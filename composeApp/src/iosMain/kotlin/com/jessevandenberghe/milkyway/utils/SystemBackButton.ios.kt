package com.jessevandenberghe.milkyway.utils

import androidx.compose.runtime.Composable

@Composable
actual fun HandleSystemBackButton(onBack: () -> Unit) {
    // iOS doesn't have a system back button in the traditional sense
    // This is a no-op on iOS
}
