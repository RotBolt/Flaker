import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.detekt) apply true
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.androidTest) apply false
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        toolVersion = "1.23.0"
        config.setFrom(file("${rootProject.rootDir}/config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.0")
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            xml.required.set(true)
            html.required.set(true)
            txt.required.set(true)
            sarif.required.set(true)
            md.required.set(true)
            exclude { it.file.path.contains("build") }
        }
    }
}


true // Needed to make the Suppress annotation work for the plugins block