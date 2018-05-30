package com.flink.models

import org.litote.kmongo.Data
import java.io.Serializable
import java.time.Instant
import java.util.UUID

@Data
data class LogModel(
        val user: UserModel,
        val message: String
) : Serializable {
    val id: UUID = UUID.randomUUID()
    val timestamp = Instant.now()
    var level = LogLevel.INFO
}

enum class LogLevel {
    INFO,
    DEBUG,
    WARNING,
    ERROR,
    SEVERE
}