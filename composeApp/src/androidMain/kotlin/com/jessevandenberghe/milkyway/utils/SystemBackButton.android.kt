package com.jessevandenberghe.milkyway.utils

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun HandleSystemBackButton(onBack: () -> Unit) {
    BackHandler {
        onBack()
    }
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled) {
        onBack()
    }
}
