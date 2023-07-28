import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqlDelight)
}

version = "0.0.1"

kotlin {

    jvmToolchain(17)
    android()
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
            implementation(project(":flaker-domain"))
        }
    }
    val commonTest by sourceSets.getting

    val androidMain by sourceSets.getting {
        dependsOn(commonMain)
        dependencies {
            implementation(libs.sqlDelight.android)
        }
    }
    val androidUnitTest by sourceSets.getting

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
    namespace = "io.rotlabs.flakerdata"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

sqldelight {
    databases {
        create("FlakerDatabase") {
            packageName.set("io.rotlabs.flakerdb")
        }
    }
}