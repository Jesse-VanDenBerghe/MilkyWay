package com.jessevandenberghe.milkyway.utils

import androidx.compose.runtime.Composable

@Composable
expect fun HandleSystemBackButton(onBack: () -> Unit)

@Composable
expect fun PlatformBackHandler(enabled: Boolean = true, onBack: () -> Unit)
