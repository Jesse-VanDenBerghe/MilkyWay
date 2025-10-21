package com.jessevandenberghe.milkyway.data.model

import kotlinx.datetime.Clock
import kotlin.time.Duration

data class FeedingSession(
    val id: String = generateSessionId(),
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    val elapsedFeedingTime: Duration,
    val elapsedBurpingTime: Duration,
    val bottleTotalMilliliters: Int,
    val milkConsumed: Int,
    val finalBottleRemainingMilliliters: Int,
    val sessionQuality: Float,
    val drinkingSpeed: Float
) {
    companion object {
        fun generateSessionId(): String = "${Clock.System.now().toEpochMilliseconds()}-${(0..9999).random()}"
    }
}
