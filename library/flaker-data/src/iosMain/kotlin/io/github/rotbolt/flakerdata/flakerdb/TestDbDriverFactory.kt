package io.github.rotbolt.flakerdata.flakerdb

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.inMemoryDriver

internal actual fun testDbDriverFactory(): SqlDriver = inMemoryDriver(FlakerDatabase.Schema)
