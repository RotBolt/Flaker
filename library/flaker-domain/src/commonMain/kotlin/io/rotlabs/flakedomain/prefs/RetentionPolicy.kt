package io.rotlabs.flakedomain.prefs

enum class RetentionPolicy(val value: String) {
    ONE_DAY("1 day"),
    SEVEN_DAYS("7 days"),
    FIFTEEN_DAYS("15 days"),
    THIRTY_DAYS("30 days")
}

@Suppress("MagicNumber")
fun RetentionPolicy.toMilliSeconds(): Long {
    return when (this) {
        RetentionPolicy.ONE_DAY -> MILLISECONDS_IN_DAY
        RetentionPolicy.SEVEN_DAYS -> 7 * MILLISECONDS_IN_DAY
        RetentionPolicy.FIFTEEN_DAYS -> 15 * MILLISECONDS_IN_DAY
        RetentionPolicy.THIRTY_DAYS -> 30 * MILLISECONDS_IN_DAY
    }
}

private const val MILLISECONDS_IN_DAY = 86400_000L
