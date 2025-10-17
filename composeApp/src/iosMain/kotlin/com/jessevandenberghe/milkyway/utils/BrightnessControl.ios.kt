package com.jessevandenberghe.milkyway.utils

import androidx.compose.runtime.Composable
import platform.UIKit.UIScreen

@Composable
actual fun InitializeBrightnessControl() {
    // No initialization needed for iOS
}

actual fun setBrightness(brightness: Float) {
    UIScreen.mainScreen.brightness = brightness.coerceIn(0.1, 1.0).toDouble()
}
