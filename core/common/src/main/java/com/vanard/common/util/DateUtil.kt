package com.vanard.common.util

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


fun Long.toIso8601Utc(): String {
    return Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
}