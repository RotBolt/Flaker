package io.rotlabs.flakerprefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class DataStoreFactory(private val context: Context) {

    actual fun create(): DataStore<Preferences> {
        return createDataStore { context.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath  }
    }
}