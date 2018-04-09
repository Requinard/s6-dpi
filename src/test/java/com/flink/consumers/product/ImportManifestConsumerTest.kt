package com.flink.consumers.product

import com.flink.gateway.Exchanges.PRODUCT_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Routes.IMPORT_MANIFEST
import org.junit.Before
import org.junit.Test
import java.util.UUID


class ImportManifestConsumerTest {
    lateinit var mqGateway: MQGateway

    @Before
    fun setup() {
        mqGateway = MQGateway()
    }

    @Test
    fun basicPublish() {
        val items1 = (0..100).map { UUID.randomUUID() }
        val items2 = (0..100).map { UUID.randomUUID() }
        val items3 = (0..500).map { UUID.randomUUID() }

        mqGateway.publish(PRODUCT_EXCHANGE, items1, IMPORT_MANIFEST)
        mqGateway.publish(PRODUCT_EXCHANGE, items2, IMPORT_MANIFEST)
        mqGateway.publish(PRODUCT_EXCHANGE, items3, IMPORT_MANIFEST)
    }
}