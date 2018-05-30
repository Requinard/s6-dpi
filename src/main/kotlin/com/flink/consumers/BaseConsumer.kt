package com.flink.consumers

import com.flink.gateway.DBGateway
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

    fun log(message: String, level: LogLevel = INFO) {
        val user = UserModel("SYSTEM", SYSTEM)
        val log = LogModel(user, message).apply {
            this.level = level
        }

        mqGateway.publish(QUERY_EXCHANGE, log, Routes.EMPTY)
    }
}