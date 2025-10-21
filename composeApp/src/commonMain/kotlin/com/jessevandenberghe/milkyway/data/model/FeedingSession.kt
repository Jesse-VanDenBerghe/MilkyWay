package com.jessevandenberghe.milkyway.data.model

import kotlin.time.Duration

data class FeedingSession(
    val id: String = generateSessionId(),
    val timestamp: Long = System.currentTimeMillis(),
    val elapsedFeedingTime: Duration,
    val elapsedBurpingTime: Duration,
    val bottleTotalMilliliters: Int,
    val milkConsumed: Int,
    val finalBottleRemainingMilliliters: Int,
    val sessionQuality: Float,
    val drinkingSpeed: Float
) {
    companion object {
        fun generateSessionId(): String = "${System.currentTimeMillis()}-${(0..9999).random()}"
    }
}
