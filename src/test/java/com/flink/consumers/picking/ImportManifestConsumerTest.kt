package com.flink.consumers.picking

import com.flink.gateway.DBGateway
import com.flink.gateway.Exchanges.PRODUCT_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Routes.IMPORT_MANIFEST
import com.flink.models.ProductInstanceModel
import org.junit.Before
import org.junit.Test


class ImportManifestConsumerTest {
    lateinit var mqGateway: MQGateway
    lateinit var dbGateway: DBGateway

    @Before
    fun setup() {
        mqGateway = MQGateway()
        dbGateway = DBGateway()
    }

    @Test
    fun basicPublish() {
        val items = dbGateway.productDatabase.find()

        items.toList()
                .map { model -> (0..100).map { ProductInstanceModel("NONE", model) } }
                .map { mqGateway.publish(PRODUCT_EXCHANGE, it, IMPORT_MANIFEST) }
    }
}