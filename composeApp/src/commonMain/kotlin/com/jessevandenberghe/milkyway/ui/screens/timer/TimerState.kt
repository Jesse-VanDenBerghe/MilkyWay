package com.jessevandenberghe.milkyway.ui.screens.timer

import androidx.compose.runtime.Immutable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Immutable
data class TimerState(
    val elapsedFeedingTime: Duration = Duration.ZERO,
    val elapsedBurpingTime: Duration = Duration.ZERO,
    val timingStep: TimingStep = TimingStep.SETUP,
    val bottleTotalTime: Duration = DEFAULT_BOTTLE_TOTAL_TIME,
    val bottleTotalMilliliters: Int = DEFAULT_BOTTLE_TOTAL_MILLILITERS,
    val bottleRemainingMilliliters: Int = DEFAULT_BOTTLE_TOTAL_MILLILITERS,
    val bottleTotalTimeInput: String = DEFAULT_BOTTLE_TOTAL_TIME_INPUT,
    val bottleTotalMillilitersInput: String = DEFAULT_BOTTLE_TOTAL_MILLILITERS_INPUT,
    val finalBottleRemainingMilliliters: Int = 0,
    val sessionQuality: Float = 3f,
    val drinkingSpeed: Float = 3f
) {
    companion object {
        const val DEFAULT_BOTTLE_TOTAL_MILLILITERS = 120
        const val DEFAULT_BOTTLE_TOTAL_MILLILITERS_INPUT = DEFAULT_BOTTLE_TOTAL_MILLILITERS.toString()
        val DEFAULT_BOTTLE_TOTAL_TIME = 15.minutes

        val DEFAULT_BOTTLE_TOTAL_TIME_INPUT = DEFAULT_BOTTLE_TOTAL_TIME.inWholeMinutes.toString()
    }
}

enum class TimingStep {
    SETUP, FEEDING, BURPING, SUMMARY, FINISHED
}