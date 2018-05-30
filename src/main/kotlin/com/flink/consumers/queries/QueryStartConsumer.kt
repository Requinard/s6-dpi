package com.flink.consumers.queries

import com.flink.consumers.BaseConsumer
import com.flink.gateway.Exchanges.QUERY_EXCHANGE
import com.flink.gateway.Queues
import com.flink.gateway.Routes.ITEM
import com.flink.models.LogLevel.ERROR
import com.flink.models.ProductModel
import com.flink.models.interim.QueryModel
import com.flink.utils.fromJson
import com.google.gson.Gson
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import java.util.UUID

object QueryStartConsumer : BaseConsumer() {
    @JvmStatic
    fun main(args: Array<String>) {
        mqGateway.consume(Queues.QUERY_START, {
            val item = Gson().fromJson<UUID>(it)

            // Find product
            val product = dbGateway.productDatabase.findOne<ProductModel>(ProductModel::id eq item)

            if (product !== null) {
                val queryId = UUID.randomUUID()
                log("Redistributing query for ${product.id}to all locations")

                dbGateway.locationDatabase.find()
                        .forEach {
                            log("Sending query to location ${it.id}")

                            mqGateway.publish(QUERY_EXCHANGE, QueryModel(
                                    product.id,
                                    queryId,
                                    it.id
                            ), ITEM)
                        }
            } else {
                log("Could not find product ${item} in database", ERROR)
            }
        })
    }

}