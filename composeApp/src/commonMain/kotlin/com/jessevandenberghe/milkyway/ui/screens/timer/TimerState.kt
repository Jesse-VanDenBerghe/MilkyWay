package com.jessevandenberghe.milkyway.ui.screens.timer

import androidx.compose.runtime.Immutable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Immutable
data class TimerState(
    val elapsedFeedingTime: Duration = Duration.ZERO,
    val elapsedBurpingTime: Duration = Duration.ZERO,
    val timingStep: TimingStep = TimingStep.IDLE,
    val bottleTotalTime: Duration = 15.minutes,
    val bottleTotalMilliliters: Int = 120
)

enum class TimingStep {
    IDLE, FEEDING, BURPING, FINISHED
}