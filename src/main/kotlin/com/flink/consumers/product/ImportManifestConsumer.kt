package com.flink.consumers.product

import com.flink.gateway.Exchanges.PRODUCT_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Queues.PRODUCT_IMPORT_MANIFEST
import com.flink.gateway.Routes.PICKER_MOVEMENT
import com.flink.models.LogLevel.INFO
import com.flink.producers.logging.LoggingProducer
import com.flink.utils.gsonUtils

/**
 * Reads a list that  was imported and redistributes it's individual components
 */
object ImportManifestConsumer {
    val mqGateway by lazy { MQGateway() }
    val logProducer by lazy { LoggingProducer() }
    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        mqGateway.consume(PRODUCT_IMPORT_MANIFEST, {
            val items = gsonUtils.decode<List<String>>(it)

            logProducer.log("Imported ${items.count()} products and redistributed it", INFO)

            items.forEach { mqGateway.publish(PRODUCT_EXCHANGE, gsonUtils.encode(it), PICKER_MOVEMENT) }
        })


    }
}