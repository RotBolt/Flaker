pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.google.com")
        mavenLocal()
    }
}

rootProject.name = "Flaker"
include(":flaker-android-ui")
include(":flaker-okhttp-core")
include(":flaker-ktor-core")
include(":flaker-android-okhttp")
include(":flaker-android-ktor")
include(":flaker-android")
include(":flaker-data")
include(":flaker-domain")
include(":flakersampleapp")
include(":flaker-android-monitor")
