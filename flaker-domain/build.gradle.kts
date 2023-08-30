import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    alias(libs.plugins.androidLibrary)
}


version = "0.1.0"

kotlin {

    jvmToolchain(17)
    android()
    val frameworkName = "FlakerDomainModule"
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
            baseName = "FlakerDomainModule"

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
    namespace = "io.rotlabs.flakerdomain"
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
}
