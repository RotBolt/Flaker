package io.github.rotbolt.flakerdata.flakerdb

import app.cash.sqldelight.db.SqlDriver

expect class DbDriverFactory {
    fun createDriver(): SqlDriver
}
