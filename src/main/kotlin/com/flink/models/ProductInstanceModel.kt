package com.flink.models

import com.flink.models.InstanceStatus.ORDERED
import com.flink.utils.now
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

data class ProductInstanceModel(
        var location: String,
        val product: ProductModel
) {
    val id: UUID = UUID.randomUUID()
    val created = now()
    var status: InstanceStatus = ORDERED
    var warehouse: LocationModel? = null
}

enum class InstanceStatus {
    ORDERED,
    PICKED_STORAGE,
    STORED,
    PICKED_DELIVERY,
    PACKED,
    SENT
}