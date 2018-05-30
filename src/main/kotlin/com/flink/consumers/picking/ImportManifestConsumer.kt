package com.flink.consumers.picking

import com.beust.klaxon.Klaxon
import com.flink.consumers.BaseConsumer
import com.flink.gateway.DBGateway
import com.flink.gateway.Exchanges.PRODUCT_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Queues.PRODUCT_IMPORT_MANIFEST
import com.flink.gateway.Routes.PICKER_MOVEMENT
import com.flink.models.InstanceStatus.PICKED_DELIVERY
import com.flink.models.LogLevel.INFO
import com.flink.models.ProductInstanceModel
import com.flink.producers.logging.LoggingProducer
import com.flink.utils.fromJson
import com.google.gson.Gson
import org.litote.kmongo.save


/**
 * Reads a list that  was imported and redistributes it's individual components
 */
object ImportManifestConsumer: BaseConsumer() {
    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        mqGateway.consume(PRODUCT_IMPORT_MANIFEST, {
            val items = Gson().fromJson<List<ProductInstanceModel>>(it)?: emptyList()

            items.forEach {
                it.status = PICKED_DELIVERY
                dbGateway.productInstanceDatabase.save(it)
                mqGateway.publish(PRODUCT_EXCHANGE, it, PICKER_MOVEMENT)
            }

            log("Imported ${items.count()} products and redistributed it", INFO)
        })
    }
}