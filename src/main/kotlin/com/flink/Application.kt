package com.flink

import com.flink.consumers.logging.DatabaseLoggerConsumer
import com.flink.consumers.logging.LogOutputConsumer
import com.flink.consumers.logging.ProrityLogConsumer

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        val applications = listOf(
                Runnable { DatabaseLoggerConsumer.main() },
                Runnable { LogOutputConsumer.main() },
                Runnable { ProrityLogConsumer.main() }
        ).map { Thread(it) }.forEach { it.run() }

        while (true) {
            Thread.sleep(100)
        }
    }
}