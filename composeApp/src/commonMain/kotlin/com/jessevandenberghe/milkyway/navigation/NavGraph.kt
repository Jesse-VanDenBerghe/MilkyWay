package com.jessevandenberghe.milkyway.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.jessevandenberghe.milkyway.ui.screens.editsession.EditSessionScreen
import com.jessevandenberghe.milkyway.ui.screens.timeline.TimelineScreen
import com.jessevandenberghe.milkyway.ui.screens.timer.TimerScreen

@Composable
fun NavGraph() {
    val currentRoute = remember { mutableStateOf<Route>(Route.Timeline) }

    when (val route = currentRoute.value) {
        Route.Timeline -> {
            TimelineScreen(
                onStartSession = {
                    currentRoute.value = Route.Timer
                },
                onEditSession = { sessionId ->
                    currentRoute.value = Route.EditSession(sessionId)
                }
            )
        }
        Route.Timer -> {
            TimerScreen(
                onBack = {
                    currentRoute.value = Route.Timeline
                }
            )
        }
        is Route.EditSession -> {
            EditSessionScreen(
                sessionId = route.sessionId,
                onBack = {
                    currentRoute.value = Route.Timeline
                }
            )
        }
    }
}
