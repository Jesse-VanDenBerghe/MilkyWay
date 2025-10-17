package com.jessevandenberghe.milkyway.ui.screens.timer.components.cards

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun ExpandingCard(
    isExpanded: Boolean,
    isActive: Boolean,
    activeContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    modifier: Modifier = Modifier,
    expandedContent: @Composable () -> Unit,
    collapsedContent: @Composable () -> Unit
) {

    val elevation by animateDpAsState(
        targetValue = if (isExpanded) 8.dp else 4.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
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
                    (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)) +
                            slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessVeryLow
                                )
                            ) { it / 2 })
                        .togetherWith(
                            fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                                    slideOutVertically(
                                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                                    ) { -it / 2 }
                        )
                } else {
                    // Collapsing
                    (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                            slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ) { -it / 2 })
                        .togetherWith(
                            fadeOut(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)) +
                                    slideOutVertically(
                                        animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
                                    ) { it / 2 }
                        )
                }
            },
            label = "cardContentAnimation"
        ) { expanded ->
            if (expanded) {
                expandedContent()
            } else {
                collapsedContent()
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
        FeedingCard(
            elapsedTime = Duration.ZERO,
            bottleTotalTime = 15.minutes,
            bottleTotalMilliliters = 120,
            isActive = true,
            isExpanded = true
        )

        BurpingCard(
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
