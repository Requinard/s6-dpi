package com.flink.models.interim

import java.util.UUID

data class QueryModel(
        val product: UUID,
        val queryId: UUID,
        val location: UUID,
        var count: Int = 0
) {
}