package com.flink.models

import org.litote.kmongo.Data
import java.io.Serializable
import java.util.UUID

@Data
data class LocationModel(
        val name: String,
        val adress: String
) : Serializable {
    val id = UUID.randomUUID()
}