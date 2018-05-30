package com.flink.utils

import org.joda.time.DateTime
import java.sql.Timestamp
import java.time.Instant

fun now() = Instant.now()

fun Timestamp.toDatetime() = DateTime(this)