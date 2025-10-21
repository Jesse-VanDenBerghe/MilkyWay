package com.jessevandenberghe.milkyway.utils

import androidx.compose.runtime.Composable

@Composable
expect fun HandleSystemBackButton(onBack: () -> Unit)
