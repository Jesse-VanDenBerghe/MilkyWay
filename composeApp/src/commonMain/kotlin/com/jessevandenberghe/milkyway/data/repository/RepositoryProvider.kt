package com.jessevandenberghe.milkyway.data.repository

object RepositoryProvider {
    private val _sessionRepository: ISessionRepository = SessionRepository()
    
    fun getSessionRepository(): ISessionRepository = _sessionRepository
}
