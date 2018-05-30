package com.flink.models.interim

import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.UUID

data class OrderModel (
        val id: UUID = UUID.randomUUID(),
        val productId: UUID,
        var done: Boolean = false,
        var success: Boolean = false
)