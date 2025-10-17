package com.jessevandenberghe.milkyway.ui.screens.timer

import androidx.compose.runtime.Immutable
import kotlin.time.Duration

@Immutable
data class TimerState(
    val elapsedFeedingTime: Duration = Duration.ZERO,
    val elapsedBurpingTime: Duration = Duration.ZERO,
    val timingStep: TimingStep = TimingStep.IDLE
)

enum class TimingStep {
    IDLE, FEEDING, BURPING, FINISHED
}