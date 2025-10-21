package com.jessevandenberghe.milkyway.ui.screens.timeline.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jessevandenberghe.milkyway.ui.utils.formatTimeDifference
import kotlin.time.Duration

@Composable
fun TimelineSeparator(
    timeDifference: Duration,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left line
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        // Time difference badge
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "⏱ ${formatTimeDifference(timeDifference)}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Right line
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
    }
}
