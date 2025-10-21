package com.jessevandenberghe.milkyway.utils

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@Composable
actual fun HandleSystemBackButton(onBack: () -> Unit) {
    // BackHandler from androidx.activity.compose handles iOS swipe-back gestures
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    if (enabled) {
        // Detect swipe-back gesture (left edge swipe to right)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        // Swipe from left edge to right
                        if (dragAmount > 50f && change.position.x < 50f) {
                            onBack()
                        }
                    }
                }
        )
    }
}
