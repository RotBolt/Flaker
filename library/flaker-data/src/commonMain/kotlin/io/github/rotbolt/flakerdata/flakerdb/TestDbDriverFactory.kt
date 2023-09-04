package io.github.rotbolt.flakerdata.flakerdb

import app.cash.sqldelight.db.SqlDriver

internal expect fun testDbDriverFactory(): SqlDriver
