package com.flink.consumers.picking

import com.beust.klaxon.Klaxon
import com.flink.gateway.DBGateway
import com.flink.gateway.MQGateway
import com.flink.gateway.Queues.PRODUCT_PICK_TO_WAREHOUSE
import com.flink.models.InstanceStatus.STORED
import com.flink.models.LogLevel.INFO
import com.flink.models.ProductInstanceModel
import com.flink.producers.logging.LoggingProducer
import org.litote.kmongo.updateOne
import java.util.Random

object PickerToWarehouseConsumer {
    val mqGateway by lazy { MQGateway() }
    val dbGateway by lazy { DBGateway() }
    val logProducer by lazy { LoggingProducer() }
    val warehouse by lazy { dbGateway.locationDatabase.find().firstOrNull() }

    var alphabet = (65..90).map { it.toChar() }
    var numbers = (1..1000).toList()
    val random = Random()
    fun random(list: List<Any>) = list.shuffled(random).take(1)[0]

    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        mqGateway.consume(PRODUCT_PICK_TO_WAREHOUSE, {
            println(it)
            val product = Klaxon().parse<ProductInstanceModel>(it)
            if (product !== null) {
                logProducer.log("Picked picking $it", INFO)

                Thread.sleep(500) // simulate get period

                product.location = "${random(alphabet)}-${ random(numbers)}"
                product.warehouse = warehouse
                product.status = STORED

                dbGateway.productInstanceDatabase.updateOne(product)
            }
        })
    }
}