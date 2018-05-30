package com.flink.models.interim

import org.litote.kmongo.Data
import java.util.UUID

@Data
data class QueryModel(
        val product: UUID,
        val queryId: UUID,
        val location: UUID,
        var count: Int = 0
) {
}