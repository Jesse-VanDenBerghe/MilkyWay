package com.jessevandenberghe.milkyway.ui.screens.timer.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration

@Composable
fun TimerCard(
    title: String,
    elapsedTime: Duration,
    isActive: Boolean,
    isExpanded: Boolean = true,
    activeContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    activeTimeColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    val elevation by animateDpAsState(
        targetValue = if (isExpanded) 8.dp else 4.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "elevation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) activeContainerColor else MaterialTheme.colorScheme.surface
        )
    ) {
        AnimatedContent(
            targetState = isExpanded,
            transitionSpec = {
                if (targetState) {
                    // Expanding
                    (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                            slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ) { it / 2 })
                        .togetherWith(
                            fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) +
                                    slideOutVertically { -it / 2 }
                        )
                } else {
                    // Collapsing
                    (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) +
                            slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                )
                            ) { -it / 2 })
                        .togetherWith(
                            fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                                    slideOutVertically { it / 2 }
                        )
                }
            },
            label = "cardContentAnimation"
        ) { expanded ->
            if (expanded) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = formatTime(elapsedTime),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) activeTimeColor else MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = formatTime(elapsedTime),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun TimerCardPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TimerCard(
            title = "Feeding",
            elapsedTime = Duration.ZERO,
            isActive = true,
            isExpanded = true
        )

        TimerCard(
            title = "Feeding",
            elapsedTime = Duration.ZERO,
            isActive = false,
            isExpanded = false
        )
    }
}

private fun formatTime(duration: Duration): String {
    val totalSeconds = duration.inWholeSeconds
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return buildString {
        if (minutes < 10) append('0')
        append(minutes)
        append(':')
        if (seconds < 10) append('0')
        append(seconds)
    }
}
