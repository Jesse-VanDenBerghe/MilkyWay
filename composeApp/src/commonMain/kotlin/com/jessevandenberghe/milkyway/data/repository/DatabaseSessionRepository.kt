package com.jessevandenberghe.milkyway.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jessevandenberghe.milkyway.data.model.FeedingSession
import com.jessevandenberghe.milkyway.database.MilkyWayDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.milliseconds

class DatabaseSessionRepository(private val database: MilkyWayDatabase) : ISessionRepository {
    
    override suspend fun saveSession(session: FeedingSession) {
        database.feedingSessionQueries.insertSession(
            id = session.id,
            timestamp = session.timestamp,
            elapsedFeedingTimeMillis = session.elapsedFeedingTime.inWholeMilliseconds,
            elapsedBurpingTimeMillis = session.elapsedBurpingTime.inWholeMilliseconds,
            bottleTotalMilliliters = session.bottleTotalMilliliters.toLong(),
            milkConsumed = session.milkConsumed.toLong(),
            finalBottleRemainingMilliliters = session.finalBottleRemainingMilliliters.toLong(),
            sessionQuality = session.sessionQuality.toDouble(),
            drinkingSpeed = session.drinkingSpeed.toDouble()
        )
    }

    override fun getSessions(): Flow<List<FeedingSession>> {
        return database.feedingSessionQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { rows ->
                rows.map { row ->
                    FeedingSession(
                        id = row.id,
                        timestamp = row.timestamp,
                        elapsedFeedingTime = row.elapsedFeedingTimeMillis.milliseconds,
                        elapsedBurpingTime = row.elapsedBurpingTimeMillis.milliseconds,
                        bottleTotalMilliliters = row.bottleTotalMilliliters.toInt(),
                        milkConsumed = row.milkConsumed.toInt(),
                        finalBottleRemainingMilliliters = row.finalBottleRemainingMilliliters.toInt(),
                        sessionQuality = row.sessionQuality.toFloat(),
                        drinkingSpeed = row.drinkingSpeed.toFloat()
                    )
                }
            }
    }
}
