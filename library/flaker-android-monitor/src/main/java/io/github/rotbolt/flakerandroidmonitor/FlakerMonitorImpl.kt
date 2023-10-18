package io.github.rotbolt.flakerandroidmonitor

import android.content.Context
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid

class FlakerMonitorImpl : FlakerMonitor {
    override fun initialize(appContext: Context) {
        SentryAndroid.init(appContext) { options ->
            options.dsn = BuildConfig.SENTRY_DSN
            options.beforeSend = SentryOptions.BeforeSendCallback { event: SentryEvent, _ -> filterEvent(event) }
        }
    }

    private fun filterEvent(event: SentryEvent): SentryEvent? {
        return event.throwable?.stackTrace?.let { stackTrace ->
            if (stackTrace.any { it.className.startsWith("io.github.rotbolt") }) {
                event
            } else {
                null
            }
        }
    }

    override fun captureException(throwable: Throwable, data: Map<String, Any?>?) {
        val exceptionEvent = SentryEvent(throwable).apply {
            data?.forEach { (key, value) ->
                if (value != null) {
                    setExtra(key, value)
                }
            }
        }
        Sentry.captureEvent(exceptionEvent)
    }
}
