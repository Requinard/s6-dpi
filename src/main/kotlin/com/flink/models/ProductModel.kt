package com.flink.models

import java.util.UUID

data class ProductModel(
        val name: String
) {
    val id: UUID = UUID.randomUUID()
}