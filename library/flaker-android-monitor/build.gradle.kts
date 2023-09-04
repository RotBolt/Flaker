import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.gradle.mavenpublish)
}

val secretsPropertiesFile = rootProject.file("secrets.properties")
val secretProperties = Properties()
secretProperties.load(FileInputStream(secretsPropertiesFile))

version = "0.1.0"

android {
    namespace = "io.github.rotbolt.flakerandroidmonitor"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "SENTRY_DSN", "\"${secretProperties["SENTRY_DSN"]}\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.sentry.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

publishing {
    repositories {

        val secretsPropertiesFile = rootProject.file("secrets.properties")
        val secretProperties = Properties()
        secretProperties.load(FileInputStream(secretsPropertiesFile))

        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/rotbolt/flaker")
            credentials {
                username = secretProperties["GPR_USERNAME"]?.toString()
                password = secretProperties["GPR_TOKEN"]?.toString()
            }
        }
    }
}