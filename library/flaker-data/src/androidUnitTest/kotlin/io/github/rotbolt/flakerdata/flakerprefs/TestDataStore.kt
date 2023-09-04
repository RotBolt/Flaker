package io.github.rotbolt.flakerdata.flakerprefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal actual fun testDataStore() : DataStore<Preferences> {
    return createDataStore { DATASTORE_FILE_NAME }
}