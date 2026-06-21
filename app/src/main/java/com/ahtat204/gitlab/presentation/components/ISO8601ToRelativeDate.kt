package com.ahtat204.gitlab.presentation.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.logging.Logger
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
fun iso8601ToRelative(iso8601: String): String {
    return try {
        val parsedDateTime = OffsetDateTime.parse(iso8601, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val now = OffsetDateTime.now(ZoneId.systemDefault())
        val secondsDiff = ChronoUnit.SECONDS.between(now, parsedDateTime)
        when {
            secondsDiff == 0L -> "just now"
            secondsDiff > 0 -> formatRelative(secondsDiff, future = true)
            else -> formatRelative(abs(secondsDiff), future = false)
        }
    } catch (e: Exception) {
       Log.d("invalid Date Format","\"Invalid date format. Expected ISO 8601 with offset (e.g., 2026-04-22T17:41:15+02:00)\"")
        throw e
    }
}

private fun formatRelative(seconds: Long, future: Boolean): String {
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val timeStr = when {
        days > 0 -> "$days day${if (days > 1) "s" else ""}"
        hours > 0 -> "$hours hour${if (hours > 1) "s" else ""}"
        minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""}"
        else -> "$seconds second${if (seconds > 1) "s" else ""}"
    }
    return if (future) "in $timeStr" else "$timeStr ago"
}