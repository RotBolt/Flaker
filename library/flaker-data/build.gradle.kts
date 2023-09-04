import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.gradle.mavenpublish)
}

version = "0.1.0"

kotlin {

    jvmToolchain(11)
    android {
        publishAllLibraryVariants()
    }
    val frameworkName = "FlakerDataModule"
    val xcFramework = XCFramework(frameworkName)

    ios() // Necessary for iosMain
    val iosX64 = iosX64() // x86 simulator
    val iosArm64 = iosArm64() // arm64 devices
    val iosSimulatorArm64 = iosSimulatorArm64() // M1 simulator
    configure(listOf(iosX64, iosArm64, iosSimulatorArm64)) {
        binaries.framework {
            baseName = frameworkName
            embedBitcode("disable")
            xcFramework.add(this)
        }
    }

    val commonMain by sourceSets.getting {
        dependencies {
            implementation(libs.sqlDelight.primitive.adapters)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.sqlDelight.coroutines.extensions)
            implementation(libs.datastore.preferences.core)
            implementation(libs.kotlinx.datetime)
            implementation(project(":library:flaker-domain"))
        }
    }
    val commonTest by sourceSets.getting {
        dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
            implementation(libs.kotlinx.datetime)
        }
    }

    val androidMain by sourceSets.getting {
        dependsOn(commonMain)
        dependencies {
            implementation(libs.sqlDelight.android)
            implementation(libs.sqlDelight.jvm)
        }
    }
    val androidUnitTest by sourceSets.getting {
        dependencies {
            implementation(libs.junit)
        }
    }

    val iosMain by sourceSets.getting {
        dependsOn(commonMain)
        dependencies {
            implementation(libs.sqlDelight.native)
        }
    }
    val iosTest by sourceSets.getting
    val iosSimulatorArm64Main by sourceSets.getting
    val iosSimulatorArm64Test by sourceSets.getting

    // Set up dependencies between the source sets
    iosSimulatorArm64Main.dependsOn(iosMain)
    iosSimulatorArm64Test.dependsOn(iosTest)

    val podAttribute = Attribute.of("pod", String::class.java)

    configurations.filter { it.name.contains("pod", ignoreCase = true) }.forEach {
        it.attributes {
            attribute(podAttribute, "pod")
        }
    }

    cocoapods {
        // Required properties
        summary = "Multiplatform Kotlin flaker library"
        homepage = "https://github.com/RotBolt/Flaker"

        framework {
            // Required properties
            baseName = "FlakerDataModule"

            // Optional properties
            isStatic = true
            transitiveExport = false
            embedBitcode(org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.DISABLE)
        }

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.RELEASE
    }
}

android {
    namespace = "io.github.rotbolt.flakerdata"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
        targetSdk = 33
        consumerProguardFiles("consumer-rules.pro")
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

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
}

sqldelight {
    databases {
        create("FlakerDatabase") {
            packageName.set("io.github.rotbolt.flakerdata.flakerdb")
        }
    }
}

mavenPublishing {
    publishing {
        repositories {

            val secretsPropertiesFile = rootProject.file("secrets.properties")
            val secretProperties = Properties()
            secretProperties.load(FileInputStream(secretsPropertiesFile))


            mavenCentral {
                signAllPublications()
            }

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
}