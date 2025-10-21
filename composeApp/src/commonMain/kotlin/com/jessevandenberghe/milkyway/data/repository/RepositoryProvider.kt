package com.jessevandenberghe.milkyway.data.repository

object RepositoryProvider {
    private var _sessionRepository: ISessionRepository? = null
    private var isInitialized = false
    
    fun initialize(sessionRepository: ISessionRepository) {
        if (!isInitialized) {
            _sessionRepository = sessionRepository
            isInitialized = true
        }
    }
    
    fun getSessionRepository(): ISessionRepository {
        return _sessionRepository ?: SessionRepository().also { _sessionRepository = it }
    }
}
