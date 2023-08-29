package io.rotlabs.flakerandroidmonitor.di

import android.content.Context
import io.rotlabs.flakerandroidmonitor.FlakerMonitor
import io.rotlabs.flakerandroidmonitor.FlakerMonitorImpl

open class FlakerAndroidMonitorContainer(appContext: Context) {

    open val flakerMonitor: FlakerMonitor by lazy {
        FlakerMonitorImpl().apply {
            initialize(appContext)
        }
    }
}
