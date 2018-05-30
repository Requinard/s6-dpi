package com.flink.consumers.product

import com.beust.klaxon.Klaxon
import com.flink.gateway.DBGateway
import com.flink.gateway.Exchanges.PRODUCT_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Queues.PRODUCT_IMPORT_MANIFEST
import com.flink.gateway.Routes.PICKER_MOVEMENT
import com.flink.models.InstanceStatus.PICKED_DELIVERY
import com.flink.models.LogLevel.INFO
import com.flink.models.ProductInstanceModel
import com.flink.producers.logging.LoggingProducer


/**
 * Reads a list that  was imported and redistributes it's individual components
 */
object ImportManifestConsumer {
    val mqGateway by lazy { MQGateway() }
    val dbGateway by lazy { DBGateway() }
    val logProducer by lazy { LoggingProducer() }
    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        mqGateway.consume(PRODUCT_IMPORT_MANIFEST, {
            val items = Klaxon().parseArray<ProductInstanceModel>(it) ?: emptyList()

            items.forEach {
                it.status = PICKED_DELIVERY
                dbGateway.productInstanceDatabase.insertOne(it)
                mqGateway.publish(PRODUCT_EXCHANGE, it, PICKER_MOVEMENT)
            }

            logProducer.log("Imported ${items.count()} products and redistributed it", INFO)
        })
    }
}