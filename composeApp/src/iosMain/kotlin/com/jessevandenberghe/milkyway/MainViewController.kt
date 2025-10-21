package com.jessevandenberghe.milkyway

import androidx.compose.ui.window.ComposeUIViewController
import com.jessevandenberghe.milkyway.data.repository.DatabaseSessionRepository
import com.jessevandenberghe.milkyway.data.repository.RepositoryProvider
import com.jessevandenberghe.milkyway.database.DatabaseFactory
import com.jessevandenberghe.milkyway.database.createDatabase

fun MainViewController() = ComposeUIViewController {
    // Initialize database and repository provider
    val databaseFactory = DatabaseFactory()
    val database = createDatabase(databaseFactory)
    val sessionRepository = DatabaseSessionRepository(database)
    RepositoryProvider.initialize(sessionRepository)
    
    App()
}
