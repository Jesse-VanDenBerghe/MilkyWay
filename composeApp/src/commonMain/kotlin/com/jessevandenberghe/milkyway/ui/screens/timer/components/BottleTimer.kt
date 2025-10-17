package com.jessevandenberghe.milkyway.ui.screens.timer.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottleTimer(
    totalMilliliters: Int,
    remainingMilliliters: Int,
    activeColor: Color,
    modifier: Modifier = Modifier
) {
    val progress = if (totalMilliliters > 0) {
        remainingMilliliters.toFloat() / totalMilliliters.toFloat()
    } else {
        0f
    }.coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = "bottleProgress"
    )

    // Calculate markings (every 10ml that divides evenly into total, max 8 markings)
    val markings = buildList {
        // Start with 10ml intervals
        var interval = 10
        var count = (totalMilliliters / interval) - 1 // Exclude 0 and total
        
        // If we have too many markings, increase interval
        while (count > 8) {
            interval += 10
            count = (totalMilliliters / interval) - 1
        }
        
        // Add markings at the calculated interval
        var current = interval
        while (current < totalMilliliters && size < 8) {
            add(current)
            current += interval
        }
    }.let { list ->
        // Ensure at least 1 marking if total > 10
        if (list.isEmpty() && totalMilliliters > 10) {
            listOf(totalMilliliters / 2)
        } else {
            list
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "${remainingMilliliters}ml",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = activeColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .width(60.dp)
                .height(200.dp)
        ) {
            // Bottle container with markings
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .border(
                        width = 3.dp,
                        color = activeColor.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .background(Color.Transparent)
            ) {
                // Markings (drawn first, so they appear behind the milk)
                markings.forEach { ml ->
                    val markingProgress = ml.toFloat() / totalMilliliters.toFloat()
                    val offsetFromBottom = 200.dp * markingProgress
                    
                    // Left marking line
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(1.5.dp)
                            .align(Alignment.BottomStart)
                            .offset(y = -offsetFromBottom)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    )
                    
                    // Right marking line
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(1.5.dp)
                            .align(Alignment.BottomEnd)
                            .offset(y = -offsetFromBottom)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    )
                    
                    // Label for marking (centered)
                    Text(
                        text = ml.toString(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = -offsetFromBottom + 6.dp)
                    )
                }
                
                // Milk level with flat top (drawn on top of markings)
                Box(
                    modifier = Modifier
                        .fillMaxHeight(animatedProgress)
                        .width(60.dp)
                        .align(Alignment.BottomCenter)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 30.dp,
                                bottomEnd = 30.dp,
                                topStart = 0.dp,
                                topEnd = 0.dp
                            )
                        )
                        .background(activeColor.copy(alpha = 0.3f))
                )
            }
        }

        Text(
            text = "0ml",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
