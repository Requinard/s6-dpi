package com.flink.utils

import org.joda.time.DateTime
import java.sql.Timestamp
import java.time.Instant

fun now() = Timestamp.from(Instant.now())

fun Timestamp.toDatetime() = DateTime(this)