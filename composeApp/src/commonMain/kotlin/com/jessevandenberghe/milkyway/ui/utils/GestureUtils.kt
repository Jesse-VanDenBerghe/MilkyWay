package com.jessevandenberghe.milkyway.ui.utils

import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.unit.IntSize
import kotlin.math.abs

/**
 * Handles horizontal swipe gestures for brightness control
 */
fun handleHorizontalSwipe(
    dragAmount: androidx.compose.ui.geometry.Offset,
    size: IntSize,
    currentBrightness: Float,
    onBrightnessChange: (Float) -> Unit,
    onStartAdjusting: () -> Unit
) {
    onStartAdjusting()
    val adjustment = dragAmount.x / size.width.toFloat() * 0.5f
    val newBrightness = (currentBrightness + adjustment).coerceIn(0.0f, 1f)
    onBrightnessChange(newBrightness)
}

/**
 * Detects if a drag gesture is primarily vertical
 */
fun isVerticalDrag(dragAmount: androidx.compose.ui.geometry.Offset): Boolean {
    return abs(dragAmount.y) > abs(dragAmount.x)
}

/**
 * Detects if a drag gesture is primarily horizontal
 */
fun isHorizontalDrag(dragAmount: androidx.compose.ui.geometry.Offset): Boolean {
    return abs(dragAmount.x) > abs(dragAmount.y)
}

/**
 * Detects swipe up gesture (threshold: -50px)
 */
fun isSwipeUp(dragAmount: androidx.compose.ui.geometry.Offset, threshold: Float = -50f): Boolean {
    return dragAmount.y < threshold && abs(dragAmount.y) > abs(threshold)
}

/**
 * Detects swipe down gesture (threshold: 50px)
 */
fun isSwipeDown(dragAmount: androidx.compose.ui.geometry.Offset, threshold: Float = 50f): Boolean {
    return dragAmount.y > threshold && abs(dragAmount.y) > threshold
}
