package com.flink.consumers.logging

import com.flink.gateway.DBGateway
import com.flink.gateway.MQGateway
import com.flink.gateway.Queues
import com.flink.models.LogModel
import com.flink.utils.gsonUtils

/**
 * Takes messages from the log queue and delivers it to the Log database
 */
object DatabaseLoggerConsumer {
    @JvmStatic
    fun main(args: Array<String> = emptyArray()) {
        mqGateway.consume(Queues.LOGS_DB_QUEUE, {
            val logModel = gsonUtils.decode<LogModel>(it)

            db.insertOne(logModel)
        })
    }

    val dbGateway by lazy { DBGateway() }
    val mqGateway by lazy { MQGateway("user", "pass") }
    val db by lazy { dbGateway.logDatabase }
}