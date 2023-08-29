package io.rotlabs.flakerokhttpcore

import android.content.Context
import io.rotlabs.flakerandroidmonitor.FlakerMonitor
import io.rotlabs.flakerandroidmonitor.di.FlakerAndroidMonitorContainer

class TestMonitorContainer : FlakerAndroidMonitorContainer(FakeContext()) {

    override val flakerMonitor: FlakerMonitor by lazy {
        object : FlakerMonitor {
            override fun initialize(appContext: Context) = Unit

            override fun captureException(throwable: Throwable, data: Map<String, Any?>?) = Unit
        }
    }
}
