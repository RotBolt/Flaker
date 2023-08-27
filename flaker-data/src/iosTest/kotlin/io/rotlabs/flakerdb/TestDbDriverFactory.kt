package io.rotlabs.flakerdb

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.inMemoryDriver
import io.rotlabs.flakerdb.FlakerDatabase

internal actual fun testDbDriverFactory(): SqlDriver = inMemoryDriver(FlakerDatabase.Schema)