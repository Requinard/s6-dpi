package com.flink.consumers.order

import com.flink.consumers.BaseConsumer
import com.flink.gateway.Exchanges.ORDER_EXCHANGE
import com.flink.gateway.Queues.ORDER_START
import com.flink.gateway.Routes.ITEM
import com.flink.models.interim.OrderModel
import com.flink.utils.fromJson
import com.google.gson.Gson
import java.util.UUID

object OrderStart: BaseConsumer() {
    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        mqGateway.consume(ORDER_START, {
            val productIds = Gson().fromJson<List<UUID>>(it)

            val orderId = UUID.randomUUID()

            log("Started picking order $orderId")

            productIds.forEach {
                val order = OrderModel(orderId, it)
                mqGateway.publish(ORDER_EXCHANGE, order, ITEM)
            }

            log("Finished distributing pickers for $orderId")
        })
    }
}