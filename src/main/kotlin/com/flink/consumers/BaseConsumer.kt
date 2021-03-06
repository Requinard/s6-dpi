package com.flink.consumers

import com.flink.gateway.DBGateway
import com.flink.gateway.Exchanges.LOG_EXCHANGE
import com.flink.gateway.Exchanges.QUERY_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Routes
import com.flink.models.LogLevel
import com.flink.models.LogLevel.INFO
import com.flink.models.LogModel
import com.flink.models.UserModel
import com.flink.models.UserModelType.SYSTEM
import kotlin.reflect.jvm.internal.impl.util.ModuleVisibilityHelper.EMPTY

abstract class BaseConsumer {
    val mqGateway = MQGateway()
    val dbGateway = DBGateway()

    fun log(message: String, level: LogLevel = INFO, user: UserModel = UserModel("SYSTEM", SYSTEM)) {
        val log = LogModel(user, message).apply {
            this.level = level
        }

        mqGateway.publish(LOG_EXCHANGE, log, Routes.EMPTY)
    }

    fun logAsRandomUser(message: String, logLevel: LogLevel = INFO){
        val user = dbGateway.userDatabase.find().shuffled().first()

        log(message, logLevel, user)
    }
}