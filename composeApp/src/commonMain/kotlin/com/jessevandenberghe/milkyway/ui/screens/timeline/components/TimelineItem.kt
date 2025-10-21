package com.jessevandenberghe.milkyway.ui.screens.timeline.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jessevandenberghe.milkyway.data.model.FeedingSession
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TimelineItem(
    session: FeedingSession,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    timelineLineHeight: Int = 80
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        // Timeline column with dot and line
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(timelineLineHeight.dp),
            contentAlignment = Alignment.Center
        ) {
            // Vertical line - adjust for first and last items
            // Hide line if this is both first and last (only session)
            if (!(isFirst && isLast)) {
                val lineHeight = when {
                    isFirst -> (timelineLineHeight / 2).dp
                    isLast -> (timelineLineHeight / 2).dp
                    else -> timelineLineHeight.dp
                }
                val lineAlignment = when {
                    isFirst -> Alignment.BottomCenter
                    isLast -> Alignment.TopCenter
                    else -> Alignment.Center
                }

                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(lineHeight)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .align(lineAlignment)
                )
            }

            // Timeline dot - centered on the line
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .align(Alignment.Center)
            )
        }

        // Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Time
                val instant = Instant.fromEpochMilliseconds(session.timestamp)
                val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                Text(
                    text = String.format("%02d:%02d", localDateTime.hour, localDateTime.minute),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Session details
                Text(
                    text = "Feeding: ${session.elapsedFeedingTime.inWholeMinutes}m ${session.elapsedFeedingTime.inWholeSeconds % 60}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (session.elapsedBurpingTime.inWholeSeconds > 0) {
                    Text(
                        text = "Burping: ${session.elapsedBurpingTime.inWholeMinutes}m ${session.elapsedBurpingTime.inWholeSeconds % 60}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Milk consumed
                Text(
                    text = "Consumed: ${session.milkConsumed}ml / ${session.bottleTotalMilliliters}ml",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
