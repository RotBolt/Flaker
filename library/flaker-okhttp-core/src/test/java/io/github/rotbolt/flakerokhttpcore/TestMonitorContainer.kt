package io.github.rotbolt.flakerokhttpcore

import android.content.Context
import io.github.rotbolt.flakerandroidmonitor.FlakerMonitor
import io.github.rotbolt.flakerandroidmonitor.di.FlakerAndroidMonitorContainer

class TestMonitorContainer : FlakerAndroidMonitorContainer(FakeContext()) {

    override val flakerMonitor: FlakerMonitor by lazy {
        object : FlakerMonitor {
            override fun initialize(appContext: Context) = Unit

            override fun captureException(throwable: Throwable, data: Map<String, Any?>?) = Unit
        }
    }
}
