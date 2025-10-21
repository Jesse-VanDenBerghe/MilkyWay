package com.jessevandenberghe.milkyway.navigation.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.jessevandenberghe.milkyway.ui.screens.editsession.EditSessionScreen

data class EditSessionScreenWrapper(val sessionId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        EditSessionScreen(
            sessionId = sessionId,
            onBack = {
                navigator.pop()
            }
        )
    }
}
