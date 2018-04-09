package com.flink.consumers.product

import com.flink.gateway.MQGateway
import com.flink.gateway.Queues.PRODUCT_PICK_TO_WAREHOUSE
import com.flink.models.LogLevel.INFO
import com.flink.producers.logging.LoggingProducer
import com.flink.utils.gsonUtils

object PickerToWarehouseConsumer {
    val mqGateway by lazy { MQGateway() }
    val logProducer by lazy { LoggingProducer() }

    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        mqGateway.consume(PRODUCT_PICK_TO_WAREHOUSE, {
            val it: String = gsonUtils.decode(it)

            logProducer.log("Picked product $it", INFO)

            Thread.sleep(1000) // simulate get period
        })
    }
}