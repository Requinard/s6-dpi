package com.flink.consumers.queries

import com.flink.consumers.BaseConsumer
import com.flink.gateway.Exchanges.QUERY_EXCHANGE
import com.flink.gateway.Queues
import com.flink.gateway.Routes
import com.flink.gateway.Routes.RETURN
import com.flink.models.LocationModel
import com.flink.models.LogLevel.ERROR
import com.flink.models.ProductInstanceModel
import com.flink.models.ProductModel
import com.flink.models.interim.QueryModel
import com.flink.utils.fromJson
import com.google.gson.Gson
import org.litote.kmongo.aggregate
import org.litote.kmongo.div
import org.litote.kmongo.eq
import org.litote.kmongo.find
import org.litote.kmongo.findOne
import org.litote.kmongo.match

object QueryItemConsumer : BaseConsumer() {
    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        mqGateway.consume(Queues.QUERY_ITEM, {
            // decode
            val queryModel = Gson().fromJson<QueryModel>(it)

            val location = dbGateway.locationDatabase.findOne(LocationModel::id eq queryModel.location)

            // check
            if (location == null) {
                log("Location was null! Quitting.", ERROR)
                return@consume
            }

            // Retrieve
            val instances = dbGateway.productInstanceDatabase.find(
                    ProductInstanceModel::warehouse eq location.id,
                    ProductInstanceModel::product eq queryModel.product
            ).toList()

            queryModel.count = instances.count()

            // Republish
            mqGateway.publish(QUERY_EXCHANGE.name, queryModel, queryModel.queryId.toString())
            mqGateway.publish(QUERY_EXCHANGE, queryModel, RETURN)
            log("Query for ${queryModel.location} is done")
        })
    }
}