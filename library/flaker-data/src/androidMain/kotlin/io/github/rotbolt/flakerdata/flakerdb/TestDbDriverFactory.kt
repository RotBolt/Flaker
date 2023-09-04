package io.github.rotbolt.flakerdata.flakerdb

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

internal actual fun testDbDriverFactory(): SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
    FlakerDatabase.Schema.create(it)
}