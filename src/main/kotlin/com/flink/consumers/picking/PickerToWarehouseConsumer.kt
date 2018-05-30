package com.flink.consumers.picking

import com.beust.klaxon.Klaxon
import com.flink.consumers.BaseConsumer
import com.flink.gateway.Queues.PRODUCT_PICK_TO_WAREHOUSE
import com.flink.models.InstanceStatus.STORED
import com.flink.models.ProductInstanceModel
import com.flink.producers.logging.LoggingProducer
import com.flink.utils.fromJson
import com.google.gson.Gson
import org.litote.kmongo.eq
import org.litote.kmongo.match
import org.litote.kmongo.save
import org.litote.kmongo.updateOne
import java.util.Random

object PickerToWarehouseConsumer : BaseConsumer() {
    val logProducer by lazy { LoggingProducer() }
    val warehouse by lazy { dbGateway.locationDatabase.find().firstOrNull() }

    var alphabet = (65..90).map { it.toChar() }
    var numbers = (1..1000).toList()
    val random = Random()
    fun random(list: List<Any>) = list.shuffled(random).take(1)[0]

    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        mqGateway.consume(PRODUCT_PICK_TO_WAREHOUSE, {
            val product = Gson().fromJson<ProductInstanceModel>(it)
            if (product !== null) {
                logAsRandomUser("Picker picking $it")

                Thread.sleep(500) // simulate get period

                product.location = "${random(alphabet)}-${random(numbers)}"
                product.warehouse = warehouse?.id
                product.status = STORED

                dbGateway.productInstanceDatabase.updateOne(ProductInstanceModel::id eq product.id, product)
            }
        })
    }
}