package io.rotlabs.flakerdb

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DbDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(FlakerDatabase.Schema, "flaker.db")
    }
}
