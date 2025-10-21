package com.jessevandenberghe.milkyway.navigation

sealed class Route(val route: String) {
    data object Timeline : Route("timeline")
    data object Timer : Route("timer")
    data class EditSession(val sessionId: String) : Route("edit_session/$sessionId")
}
