import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.gradle.mavenpublish)
}

version = "0.1.0"

kotlin {

    jvmToolchain(11)
    android {
        publishAllLibraryVariants()
    }
    val frameworkName = "FlakerKtorCoreModule"
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

    cocoapods {
        // Required properties
        summary = "Multiplatform Kotlin flaker library"
        homepage = "https://github.com/RotBolt/Flaker"

        framework {
            // Required properties
            baseName = "FlakerKtorCoreModule"

            // Optional properties
            isStatic = true
            transitiveExport = false
            embedBitcode(org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.DISABLE)
        }

        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["CUSTOM_DEBUG"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.RELEASE
    }

    val commonMain by sourceSets.getting {
        dependencies {
            implementation(project(":library:flaker-domain"))
            implementation(project(":library:flaker-data"))
        }
    }

    val commonTest by sourceSets.getting

    val androidMain by sourceSets.getting {
        dependencies {
            implementation(project(":library:flaker-data"))
        }
    }
    val androidUnitTest by sourceSets.getting

    val iosMain by sourceSets.getting {
        dependencies {
            implementation(project(":library:flaker-data"))
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

}

android {
    namespace = "io.rotlabs.flakerktorcore"
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