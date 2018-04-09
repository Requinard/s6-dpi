package com.flink.consumers.logging

import com.flink.gateway.Exchanges.LOG_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.models.LogModel
import com.flink.utils.gsonUtils

object LogOutputConsumer {
    @JvmStatic
    fun main(args: Array<String>) {
        val queueName = mqGateway.createExclusiveQueue(LOG_EXCHANGE)

        mqGateway.consume(queueName, {
            val logModel = gsonUtils.decode<LogModel>(it)

            println("${logModel.user.username}@${logModel.timestamp.epochSecond}:${logModel.message}")
        })
    }

    val mqGateway by lazy { MQGateway("user", "pass") }
}