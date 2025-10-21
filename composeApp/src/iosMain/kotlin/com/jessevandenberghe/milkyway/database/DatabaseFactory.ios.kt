package com.jessevandenberghe.milkyway.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.jessevandenberghe.milkyway.database.MilkyWayDatabase

actual class DatabaseFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(MilkyWayDatabase.Schema, "milkyway.db")
    }
}
