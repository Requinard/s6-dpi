package com.flink.consumers.logging

import com.flink.gateway.Exchanges.ERROR_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Queues.LOGS_HIGH_PRIORTY
import com.flink.gateway.Routes.LOG_ERROR
import com.flink.models.LogLevel.ERROR
import com.flink.models.LogModel
import com.flink.utils.gsonUtils

/**
 * Filter for high priority error messages
 */
object ProrityLogConsumer {
    val mqGateway by lazy { MQGateway() }
    @JvmStatic
    fun main(args: Array<String>) {
        mqGateway.consume(LOGS_HIGH_PRIORTY, {
            val logModel = gsonUtils.decode<LogModel>(it)

            if (logModel.level == ERROR) {
                println("ERROR ${logModel.message} @ ${logModel.timestamp}")

                mqGateway.publish(ERROR_EXCHANGE, "log-error", LOG_ERROR, 2)
            }
        })
    }
}