package com.flink.producers.logging

import com.flink.gateway.Exchanges.LOG_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Routes.EMPTY
import com.flink.models.LogLevel
import com.flink.models.LogModel
import com.flink.models.UserModel
import com.flink.models.UserModelType.SYSTEM

class LoggingProducer {
    companion object {
        val defaultUser = UserModel("LOGGING-PRODUCER", SYSTEM)
    }

    val mqGateway by lazy { MQGateway() }

    fun log(logModel: LogModel) {
        mqGateway.publish(LOG_EXCHANGE, logModel, EMPTY)
    }

    fun log(message: String, logLevel: LogLevel) = log(
            LogModel(defaultUser, message).apply { level = logLevel })

}