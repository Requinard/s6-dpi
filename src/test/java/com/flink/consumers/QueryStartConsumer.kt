package com.flink.consumers

import com.flink.gateway.DBGateway
import com.flink.gateway.Exchanges.QUERY_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Routes.SPLIT
import org.junit.Test

class QueryStartConsumer {
    val mqGateway = com.flink.gateway.MQGateway()
    val dbGateway = DBGateway()

    @Test
    fun testOne(){
        val product = dbGateway.productDatabase.find().first()

        mqGateway.publish(QUERY_EXCHANGE, product.id, SPLIT)
    }
}