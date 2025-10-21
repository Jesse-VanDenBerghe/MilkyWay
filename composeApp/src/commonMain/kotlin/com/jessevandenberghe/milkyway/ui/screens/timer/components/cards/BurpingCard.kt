package com.jessevandenberghe.milkyway.ui.screens.timer.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jessevandenberghe.milkyway.ui.screens.timer.components.CircularProgressTimer
import com.jessevandenberghe.milkyway.ui.screens.timer.components.CollapsedTimerContent
import kotlin.time.Duration

@Composable
fun BurpingCard(
    elapsedTime: Duration,
    isActive: Boolean,
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    ExpandingCard(
        isExpanded = isExpanded,
        isActive = isActive,
        activeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier,
        expandedContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "💨",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Burping Time",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                CircularProgressTimer(
                    elapsedTime = elapsedTime,
                    isActive = isActive,
                    activeTimeColor = MaterialTheme.colorScheme.secondary
                )
            }
        },
        collapsedContent = {
            CollapsedTimerContent(
                title = "Burping",
                elapsedTime = elapsedTime
            )
        }
    )
}
