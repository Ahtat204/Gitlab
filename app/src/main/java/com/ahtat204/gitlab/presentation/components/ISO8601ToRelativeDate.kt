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
/**
 * Converts an ISO 8601 date-time string with offset into a human-readable relative time string.
 *
 * ## Purpose
 * - Parses an ISO 8601 formatted timestamp (e.g., `"2026-04-22T17:41:15+02:00"`).
 * - Compares it against the current system time.
 * - Returns a relative description such as `"just now"`, `"5 minutes ago"`, or `"in 2 days"`.
 *
 * ## Parameters
 * @param iso8601 A date-time string in ISO 8601 format with offset.
 *
 * ## Returns
 * A human-readable relative time string describing how far in the past or future
 * the given timestamp is compared to now.
 *
 * ## Behavior
 * - Parses the input using [DateTimeFormatter.ISO_OFFSET_DATE_TIME].
 * - Computes the difference in seconds between now and the parsed timestamp.
 * - Returns:
 *   - `"just now"` if the difference is zero.
 *   - `"in X ..."` if the timestamp is in the future.
 *   - `"X ... ago"` if the timestamp is in the past.
 * - Logs an error if parsing fails and rethrows the exception.
 *
 * ## Example
 * ```
 * val relative = iso8601ToRelative("2026-04-22T17:41:15+02:00")
 * println(relative) // e.g., "2 months ago"
 * ```
 *
 * ## Notes
 * - Requires API level [Build.VERSION_CODES.O] for [OffsetDateTime] and [DateTimeFormatter].
 * - Supports granularity down to seconds, minutes, hours, and days.
 * - For invalid formats, logs a debug message and throws the parsing exception.
 */
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
/**
 * Formats a time difference in seconds into a relative string.
 *
 * ## Purpose
 * - Converts a raw difference in seconds into a human-readable unit (seconds, minutes, hours, days).
 * - Adds context ("ago" or "in ...") depending on whether the timestamp is past or future.
 *
 * ## Parameters
 * @param seconds The difference in seconds between two timestamps.
 * @param future Whether the timestamp is in the future (`true`) or past (`false`).
 *
 * ## Returns
 * A relative time string (e.g., `"3 hours ago"`, `"in 2 days"`).
 *
 * ## Example
 * ```
 * val result = formatRelative(3600, future = false)
 * println(result) // "1 hour ago"
 * ```
 */
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