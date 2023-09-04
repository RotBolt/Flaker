package io.github.rotbolt.flakerandroidmonitor.di

import android.content.Context
import io.github.rotbolt.flakerandroidmonitor.FlakerMonitor
import io.github.rotbolt.flakerandroidmonitor.FlakerMonitorImpl

open class FlakerAndroidMonitorContainer(appContext: Context) {

    open val flakerMonitor: FlakerMonitor by lazy {
        FlakerMonitorImpl().apply {
            initialize(appContext)
        }
    }
}
