package com.flink

import com.flink.consumers.logging.DatabaseLoggerConsumer
import com.flink.consumers.logging.LogOutputConsumer
import com.flink.consumers.logging.ProrityLogConsumer
import com.flink.consumers.picking.ImportManifestConsumer
import com.flink.consumers.picking.PickerToWarehouseConsumer
import com.flink.consumers.queries.QueryItemConsumer
import com.flink.consumers.queries.QueryStartConsumer

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        val applications = listOf(
                Runnable { DatabaseLoggerConsumer.main() },
                Runnable { LogOutputConsumer.main() },
                Runnable { ProrityLogConsumer.main() },
                Runnable { ImportManifestConsumer.main() },
                Runnable { PickerToWarehouseConsumer.main() },
                Runnable { PickerToWarehouseConsumer.main() },
                Runnable { QueryStartConsumer.main() },
                Runnable { QueryItemConsumer.main() }
        ).map { Thread(it) }.forEach { it.run() }

        while (true) {
            Thread.sleep(100)
        }
    }
}