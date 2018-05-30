package com.flink.models

import com.flink.models.InstanceStatus.ORDERED
import com.flink.utils.now
import com.sun.org.glassfish.external.statistics.TimeStatistic
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.litote.kmongo.Data
import java.sql.Time
import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

@Data
data class ProductInstanceModel(
        var location: String,
        val product: UUID,
        val id: UUID = UUID.randomUUID(),
        val created: Timestamp = now(),
        var status: InstanceStatus = ORDERED,
        var warehouse: UUID? = null
)

enum class InstanceStatus {
    ORDERED,
    PICKED_STORAGE,
    STORED,
    PICKED_DELIVERY,
    PACKED,
    SENT
}