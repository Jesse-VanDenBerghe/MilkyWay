package com.jessevandenberghe.milkyway.navigation.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.jessevandenberghe.milkyway.ui.screens.timeline.TimelineScreen

data object TimelineScreenWrapper : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        TimelineScreen(
            onStartSession = {
                navigator.push(TimerScreenWrapper)
            },
            onEditSession = { sessionId ->
                navigator.push(EditSessionScreenWrapper(sessionId))
            }
        )
    }
}
