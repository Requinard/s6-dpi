package com.flink.models

import java.io.Serializable
import java.util.UUID

data class LocationModel(
        val name: String,
        val adress: String
) : Serializable {
    val id = UUID.randomUUID()
}