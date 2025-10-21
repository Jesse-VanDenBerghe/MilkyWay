package com.jessevandenberghe.milkyway.utils

import androidx.compose.runtime.Composable

@Composable
actual fun HandleSystemBackButton(onBack: () -> Unit) {
    // BackHandler from androidx.activity.compose handles iOS swipe-back gestures
}
