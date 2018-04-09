package com.flink.consumers.logging

import com.flink.gateway.Exchanges.LOG_EXCHANGE
import com.flink.gateway.MQGateway
import com.flink.gateway.Routes.EMPTY
import com.flink.models.LogLevel.ERROR
import com.flink.models.LogModel
import com.flink.models.UserModel
import com.flink.models.UserModelType.MANAGER
import org.junit.Before
import org.junit.Test

class DatabaseLoggerConsumerTest {
    val user = UserModel("john", MANAGER)
    lateinit var mqGateway: MQGateway

    @Before
    fun setup() {
        mqGateway = MQGateway("user", "pass")
    }

    @Test
    fun basicPublish() {
        val log = LogModel(user, "hello world")

        mqGateway.publish(LOG_EXCHANGE, log, EMPTY)
    }

    @Test
    fun bigPublish() {
        for (i in 0..100000) {
            mqGateway.publish(LOG_EXCHANGE, LogModel(user, "Message $i"), EMPTY)
        }
    }

    @Test
    fun basicError(){
        val log = LogModel(user, "FUBAR").apply { level = ERROR }
        mqGateway.publish(LOG_EXCHANGE, log, EMPTY)
    }
}
