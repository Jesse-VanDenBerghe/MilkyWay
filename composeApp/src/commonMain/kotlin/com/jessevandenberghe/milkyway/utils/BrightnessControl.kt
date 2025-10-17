package com.jessevandenberghe.milkyway.utils

import androidx.compose.runtime.Composable

@Composable
expect fun InitializeBrightnessControl()

expect fun setBrightness(brightness: Float)

