package com.jessevandenberghe.milkyway.data.repository

import com.jessevandenberghe.milkyway.data.model.FeedingSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ISessionRepository {
    suspend fun saveSession(session: FeedingSession)
    suspend fun deleteSession(sessionId: String)
    fun getSessions(): Flow<List<FeedingSession>>
}

class SessionRepository : ISessionRepository {
    private val _sessions = MutableStateFlow<List<FeedingSession>>(emptyList())
    
    override suspend fun saveSession(session: FeedingSession) {
        val currentSessions = _sessions.value.toMutableList()
        currentSessions.add(0, session) // Add to beginning (most recent first)
        _sessions.value = currentSessions
    }

    override suspend fun deleteSession(sessionId: String) {
        val currentSessions = _sessions.value.toMutableList()
        currentSessions.removeAll { it.id == sessionId }
        _sessions.value = currentSessions
    }

    override fun getSessions(): Flow<List<FeedingSession>> = _sessions.asStateFlow()
}
