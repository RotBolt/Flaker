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
    }
}

rootProject.name = "Flaker"
include(":flaker-android-ui")
include(":flaker-retrofit")
include(":flaker-ktor")
