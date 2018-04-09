package com.flink.gateway

import com.flink.gateway.Exchanges.PRODUCT_EXCHANGE
import com.flink.gateway.Queues.LOGS_DB_QUEUE
import com.flink.gateway.Queues.PRODUCT_PICK_TO_WAREHOUSE
import com.flink.gateway.Routes.EMPTY
import com.flink.gateway.Routes.IMPORT_MANIFEST
import com.flink.gateway.Routes.PICKER_MOVEMENT
import com.flink.utils.gsonUtils
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.BuiltinExchangeType.DIRECT
import com.rabbitmq.client.BuiltinExchangeType.FANOUT
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope

class MQGateway(
        val username: String = "user",
        val password: String = "pass",
        val host: String = "localhost",
        val vhost: String = "vhost",
        val port: Int = 5672
) {
    private val exchangeSuffix = "EXCHANGE"
    private val queueSuffix = "QUEUE"
    private val connection by lazy {
        ConnectionFactory().apply {
            username = this@MQGateway.username
            password = this@MQGateway.password
            host = this@MQGateway.host
            virtualHost = this@MQGateway.vhost
            port = this@MQGateway.port
        }.newConnection()
    }

    val channel by lazy { connection.createChannel() }

    init {
        if (channel.isOpen) {
            // If we have a channel we can declare exchanges

            channel.apply {
                // Declare exchanges
                exchangeDeclare(Exchanges.LOG_EXCHANGE, FANOUT)
                exchangeDeclare(Exchanges.ERROR_EXCHANGE, DIRECT)
                exchangeDeclare(PRODUCT_EXCHANGE, DIRECT)

                // Declare queues
                queueDeclare(Queues.LOGS_DB_QUEUE, true, false, false, emptyMap())
                queueDeclare(Queues.LOGS_HIGH_PRIORTY, true, false, false, emptyMap())
                queueDeclare(Queues.ERROR_LOG_EMAIL, true, false, false, emptyMap())
                queueDeclare(Queues.PRODUCT_IMPORT_MANIFEST, true, false, false, emptyMap())
                queueDeclare(Queues.PRODUCT_PICK_TO_WAREHOUSE, true, false, false, emptyMap())

                // Bind queues and exchanges
                queueBind(Queues.LOGS_DB_QUEUE, Exchanges.LOG_EXCHANGE, EMPTY)
                queueBind(Queues.LOGS_HIGH_PRIORTY, Exchanges.LOG_EXCHANGE, EMPTY)
                queueBind(Queues.ERROR_LOG_EMAIL, Exchanges.ERROR_EXCHANGE, Routes.LOG_ERROR)
                queueBind(Queues.PRODUCT_IMPORT_MANIFEST, PRODUCT_EXCHANGE, IMPORT_MANIFEST)
                queueBind(PRODUCT_PICK_TO_WAREHOUSE, PRODUCT_EXCHANGE, PICKER_MOVEMENT)
            }
        }
    }

    fun exchangeDeclare(exchange: Exchanges, type: BuiltinExchangeType) = channel.exchangeDeclare(exchange.name, type)
    fun queueDeclare(queue: Queues, durable: Boolean, exclusive: Boolean, autoDelete: Boolean, map: Map<String, Any>) = channel.queueDeclare(queue.name, durable, exclusive, autoDelete, map)
    fun queueBind(queue: Queues, exchange: Exchanges, routes: Routes) = channel.queueBind(queue.name, exchange.name, routes.name)
    /**
     * Creates a non-durable queue without a routing key
     */
    fun createExclusiveQueue(exchange: Exchanges): String {
        val queue = channel.queueDeclare().queue
        channel.queueBind(queue, exchange.name, "")

        return queue
    }

    fun consume(queue: Queues, handler: (String) -> Unit) = consume(queue.name, handler)

    fun consume(queue: String, handler: (String) -> Unit) {
        val consumer = object : DefaultConsumer(channel) {
            override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: ByteArray) {
                val tag = envelope.deliveryTag
                handler(body.toString(Charsets.UTF_8))
                channel.basicAck(tag, false)
            }
        }

        channel.basicConsume(queue, false, consumer)
    }

    fun publish(
            exchange: String,
            obj: Any,
            routingKey: String = "",
            deliveryMode: Int = 2
    ) {
        val properties = AMQP
                .BasicProperties
                .Builder()
                .deliveryMode(deliveryMode)
                .build()

        val json = gsonUtils.encode(obj)
                .toByteArray(Charsets.UTF_8)

        channel.basicPublish(exchange, routingKey, properties, json)
    }

    fun publish(
            exchange: Exchanges,
            obj: Any,
            routingKey: Routes,
            deliveryMode: Int = 2
    ) = publish(exchange.name, obj, routingKey.name, deliveryMode)
}

enum class Exchanges {
    LOG_EXCHANGE,
    ERROR_EXCHANGE,
    PRODUCT_EXCHANGE
}

enum class Queues {
    LOGS_DB_QUEUE,
    LOGS_HIGH_PRIORTY,
    ERROR_LOG_EMAIL,
    PRODUCT_IMPORT_MANIFEST,
    PRODUCT_PICK_TO_WAREHOUSE
}

enum class Routes {
    EMPTY,
    LOG_ERROR,
    IMPORT_MANIFEST,
    PICKER_MOVEMENT
}