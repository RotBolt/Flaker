---
hide:
  - navigation
---

# Usage

## Installation 

Add `mavenCentral()` to repositories in your `build.gradle` file.

```kotlin
   repositories {
        mavenCentral()
   }
```

Add the following dependency to your `build.gradle`

```kotlin
   dependencies {
        debugImplementation("io.github.rotbolt:flaker-android-okhttp:${latest_version}")
        releaseImplementation("io.github.rotbolt:flaker-android-okhttp-noop:${latest_version}")
   }
```

### flaker-android-okhttp
Add the following statement to your app's onCreate method.
```kotlin
   class MainApplication: Application() {
       override fun onCreate() {
           super.onCreate()
           FlakerAndroidOkhttpContainer.install(this)
       }
   }
```

Then in your okhttp client builder, add the following interceptor.
```kotlin
    val client = OkHttpClient.Builder()
        .addInterceptor(FlakerInterceptor.Builder().build())
        .build()
```
That's it. Now upon installing your app, a companion app `flaker`  will be installed on your device. You can use this app to configure the network conditions for your app.

#### Configuration

You can configure the following parameters in the `FlakerInterceptor`.

- Failure Response by Flaker

```kotlin
    val flakerFailResponse = FlakerFailResponse(
        httpCode = 500,
        message = "Flaker is enabled. This is a flaky response.",
        responseBodyString = "Test Failure"
    )
     val flakerInterceptor = FlakerInterceptor.Builder()
        .failResponse(flakerFailResponse)
        .build()
```


### flaker-android-ktor 
In progress

### flaker-ios-ktor (In Progress)
In progress

## Releases
For the latest release versions, please check the [github releases](https://github.com/RotBolt/Flaker/releases)
