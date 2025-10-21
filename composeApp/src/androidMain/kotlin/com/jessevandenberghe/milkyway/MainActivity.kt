package com.jessevandenberghe.milkyway

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jessevandenberghe.milkyway.data.repository.DatabaseSessionRepository
import com.jessevandenberghe.milkyway.data.repository.RepositoryProvider
import com.jessevandenberghe.milkyway.database.DatabaseFactory
import com.jessevandenberghe.milkyway.database.createDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Initialize database and repository provider
        val databaseFactory = DatabaseFactory(this)
        val database = createDatabase(databaseFactory)
        val sessionRepository = DatabaseSessionRepository(database)
        RepositoryProvider.initialize(sessionRepository)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}