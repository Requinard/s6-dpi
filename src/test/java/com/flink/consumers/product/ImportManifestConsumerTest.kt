package com.flink.consumers.product

import com.flink.gateway.Exchanges.PRODUCT_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Routes.IMPORT_MANIFEST
import org.junit.Before
import org.junit.Test


class ImportManifestConsumerTest {
    lateinit var mqGateway: MQGateway

    @Before
    fun setup() {
        mqGateway = MQGateway()
    }

    @Test
    fun basicPublish() {
        val items = listOf(
                "HAHA",
                "JA"
        )
        mqGateway.publish(PRODUCT_EXCHANGE, items, IMPORT_MANIFEST)
    }
}