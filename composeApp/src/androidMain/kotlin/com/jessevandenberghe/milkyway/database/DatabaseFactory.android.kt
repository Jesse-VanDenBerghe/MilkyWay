package com.jessevandenberghe.milkyway.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.jessevandenberghe.milkyway.database.MilkyWayDatabase

actual class DatabaseFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(MilkyWayDatabase.Schema, context, "milkyway.db")
    }
}
