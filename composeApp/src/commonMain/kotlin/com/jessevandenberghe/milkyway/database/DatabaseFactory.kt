package com.jessevandenberghe.milkyway.database

import app.cash.sqldelight.db.SqlDriver
import com.jessevandenberghe.milkyway.database.MilkyWayDatabase

expect class DatabaseFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(factory: DatabaseFactory): MilkyWayDatabase {
    return MilkyWayDatabase(factory.createDriver())
}
