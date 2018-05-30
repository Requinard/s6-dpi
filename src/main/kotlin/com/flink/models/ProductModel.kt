package com.flink.models

import org.litote.kmongo.Data
import java.util.UUID

@Data
data class ProductModel(
        val name: String
) {
    val id: UUID = UUID.randomUUID()
}