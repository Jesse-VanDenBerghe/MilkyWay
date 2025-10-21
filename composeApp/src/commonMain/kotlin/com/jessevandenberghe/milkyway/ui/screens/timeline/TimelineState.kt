package com.jessevandenberghe.milkyway.ui.screens.timeline

import androidx.compose.runtime.Immutable
import com.jessevandenberghe.milkyway.data.model.FeedingSession

@Immutable
data class TimelineState(
    val sessions: List<FeedingSession> = emptyList(),
    val isLoading: Boolean = false
)
