package com.jessevandenberghe.milkyway.ui.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

/**
 * Formats a Duration into a human-readable time difference string.
 * Examples: "5s", "2m 30s", "1h 15m"
 */
fun formatTimeDifference(duration: Duration): String {
    val totalSeconds = duration.inWholeSeconds
    
    return when {
        totalSeconds < 60 -> "${totalSeconds}s"
        totalSeconds < 3600 -> {
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            if (seconds == 0L) "${minutes}m" else "${minutes}m ${seconds}s"
        }
        else -> {
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            if (minutes == 0L) "${hours}h" else "${hours}h ${minutes}m"
        }
    }
}

/**
 * Formats a timestamp (epoch milliseconds) to HH:MM time string.
 */
fun formatTime(timestampMillis: Long): String {
    val instant = Instant.fromEpochMilliseconds(timestampMillis)
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return dateTime.hour.toString().padStart(2, '0') + ":" + 
           dateTime.minute.toString().padStart(2, '0')
}
