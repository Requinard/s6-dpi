package com.flink.models

import org.litote.kmongo.Data
import java.time.Instant
import java.util.UUID

@Data
data class UserModel(
        val username: String,
        val type: UserModelType
) {
    val id: UUID = UUID.randomUUID()
    val created = Instant.now()
}

enum class UserModelType {
    PICKER,
    SUPPLIER,
    MANAGER,
    PACKER,
    USER,
    SYSTEM
}