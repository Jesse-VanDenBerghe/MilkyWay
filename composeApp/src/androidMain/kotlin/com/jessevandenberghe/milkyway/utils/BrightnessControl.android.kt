package com.jessevandenberghe.milkyway.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private var currentActivity: Activity? = null

@Composable
actual fun InitializeBrightnessControl() {
    currentActivity = LocalContext.current as Activity
}

actual fun setBrightness(brightness: Float) {
    currentActivity?.let { activity ->
        val layoutParams = activity.window.attributes
        layoutParams.screenBrightness = brightness.coerceIn(0.0f, 1f)
        activity.window.attributes = layoutParams
    }
}
