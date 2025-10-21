package com.jessevandenberghe.milkyway.navigation.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.jessevandenberghe.milkyway.ui.screens.timer.TimerScreen

data object TimerScreenWrapper : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        TimerScreen(
            onBack = {
                navigator.pop()
            }
        )
    }
}
