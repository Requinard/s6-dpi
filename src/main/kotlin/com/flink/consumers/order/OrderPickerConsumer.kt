package com.flink.consumers.order

import com.flink.consumers.BaseConsumer
import com.flink.gateway.Exchanges.ORDER_EXCHANGE
import com.flink.gateway.Queues.ORDER_ITEMS
import com.flink.gateway.Routes.RETURN
import com.flink.models.InstanceStatus.ORDERED
import com.flink.models.InstanceStatus.STORED
import com.flink.models.ProductInstanceModel
import com.flink.models.interim.OrderModel
import com.flink.utils.fromJson
import com.google.gson.Gson
import org.litote.kmongo.eq
import org.litote.kmongo.find
import org.litote.kmongo.updateOne

object OrderPickerConsumer : BaseConsumer() {
    @JvmStatic
    fun main(args: Array<String>) {
        mqGateway.consume(ORDER_ITEMS, {
            val order = Gson().fromJson<OrderModel>(it)

            val instance = dbGateway.productInstanceDatabase.find(
                    ProductInstanceModel::product eq order.productId,
                    ProductInstanceModel::status eq STORED
            ).toList()

            if (instance.isEmpty()) {
                log("Order ${order.id}: No item found for ${order.productId}")
                order.done = true

                mqGateway.publish(ORDER_EXCHANGE, order, RETURN)
                mqGateway.publish(ORDER_EXCHANGE.name, order, order.id.toString())
                return@consume
            }
            val pickedProduct = instance.first()

            log("Order ${order.id}: Shipping product instance ${pickedProduct.id} for product ${order.productId}")

            pickedProduct.status = ORDERED
            pickedProduct.location = "GONE"

            dbGateway.productInstanceDatabase.updateOne(
                    ProductInstanceModel::id eq pickedProduct.id,
                    pickedProduct
            )

            log("Order ${order.id} product ${order.productId} success")

            order.done = true
            order.success = true

            mqGateway.publish(ORDER_EXCHANGE.name, order, order.id.toString())
            mqGateway.publish(ORDER_EXCHANGE, order, RETURN)
        })
    }
}