import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.gradle.mavenpublish)
}

version = "0.1.0"

android {
    namespace = "io.rotlabs.flakerokhttpcore"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    implementation(project(":library:flaker-domain"))
    implementation(project(":library:flaker-data"))
    implementation(project(":library:flaker-android-monitor"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.retrofit.mock)
    implementation(libs.okttp)
    testImplementation(libs.mockwebserver)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
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