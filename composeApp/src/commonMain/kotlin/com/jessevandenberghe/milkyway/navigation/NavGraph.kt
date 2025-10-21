package com.jessevandenberghe.milkyway.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.jessevandenberghe.milkyway.navigation.screens.TimelineScreenWrapper

@Composable
fun NavGraph() {
    Navigator(TimelineScreenWrapper)
}
