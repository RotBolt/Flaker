package io.rotlabs

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.rotlabs.flakerdb.FlakerDatabase

internal actual fun testDbDriverFactory(): SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
    FlakerDatabase.Schema.create(it)
}