package io.github.rotbolt.flakerandroidmonitor

import android.content.Context
import io.sentry.Sentry
import io.sentry.SentryEvent
import io.sentry.SentryLevel
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid

class FlakerMonitorImpl : FlakerMonitor {
    override fun initialize(appContext: Context) {
        SentryAndroid.init(appContext) { options ->
            options.dsn = BuildConfig.SENTRY_DSN
            // Add a callback that will be used before the event is sent to Sentry.
            // With this callback, you can modify the event or, when returning null, also discard the event.
            options.beforeSend =
                SentryOptions.BeforeSendCallback { event: SentryEvent, _ ->
                    if (SentryLevel.DEBUG == event.level) {
                        null
                    } else {
                        event
                    }
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
