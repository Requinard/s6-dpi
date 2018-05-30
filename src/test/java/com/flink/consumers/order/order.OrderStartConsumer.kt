package com.flink.consumers.order

import com.flink.gateway.DBGateway
import com.flink.gateway.Exchanges.ORDER_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Routes
import org.junit.Test

class OrderStartConsumerConsumer {
    val mqGateway = MQGateway()
    val dbGateway = DBGateway()
    @Test
    fun testOne() {
        val product =dbGateway.productDatabase.find().shuffled().first()

        mqGateway.publish(ORDER_EXCHANGE, listOf(product.id), Routes.SPLIT)
    }
}