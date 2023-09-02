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
include(":library:flaker-android-ui")
include(":library:flaker-okhttp-core")
include(":library:flaker-ktor-core")
include(":library:flaker-android-okhttp")
include(":library:flaker-android-ktor")
include(":library:flaker-android")
include(":library:flaker-data")
include(":library:flaker-domain")
include(":library:flaker-android-monitor")

include("library-noop:flaker-okhttp-core-noop")

include("sampleapp")
include(":library-noop:flaker-android-okhttp-noop")
