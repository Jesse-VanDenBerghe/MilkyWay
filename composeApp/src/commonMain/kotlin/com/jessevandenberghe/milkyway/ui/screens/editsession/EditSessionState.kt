package com.jessevandenberghe.milkyway.ui.screens.editsession

import com.jessevandenberghe.milkyway.data.model.FeedingSession
import kotlin.time.Duration

data class EditSessionState(
    val session: FeedingSession? = null,
    val isLoading: Boolean = true,
    val feedingTimeInput: String = "",
    val burpingTimeInput: String = "",
    val bottleTotalMillilitersInput: String = "",
    val finalBottleRemainingMillilitersInput: String = "",
    val sessionQuality: Float = 3f,
    val drinkingSpeed: Float = 3f,
    val isSaving: Boolean = false,
    val hasChanges: Boolean = false
)
