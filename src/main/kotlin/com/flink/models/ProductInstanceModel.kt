package com.flink.models

import com.flink.models.InstanceStatus.ORDERED
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.UUID

data class ProductInstanceModel(
        val location: String,
        val product: ProductModel
) {
    val id: UUID = UUID.randomUUID()
    val created: DateTime = DateTime.now(DateTimeZone.UTC)
    val status: InstanceStatus = ORDERED
}

enum class InstanceStatus {
    ORDERED,
    PICKED_STORAGE,
    STORED,
    PICKED_DELIVERY,
    PACKED,
    SENT
}