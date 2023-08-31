package io.rotlabs.flakerprefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal expect fun testDataStore() : DataStore<Preferences>