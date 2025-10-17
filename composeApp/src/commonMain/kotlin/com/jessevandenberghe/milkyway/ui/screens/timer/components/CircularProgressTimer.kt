package com.jessevandenberghe.milkyway.ui.screens.timer.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.time.Duration

@Composable
fun CircularProgressTimer(
    elapsedTime: Duration,
    isActive: Boolean,
    activeTimeColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(180.dp)
    ) {
        val seconds = elapsedTime.inWholeSeconds % 60
        val progress = seconds / 60f
        
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            label = "progressAnimation"
        )

        Canvas(modifier = Modifier.size(180.dp)) {
            drawCircle(
                color = Color.Gray.copy(alpha = 0.2f),
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Canvas(modifier = Modifier.size(180.dp)) {
            drawArc(
                color = if (isActive) activeTimeColor else Color.Gray,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Text(
            text = formatTime(elapsedTime),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) activeTimeColor else MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
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
