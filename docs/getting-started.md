# Usage

#### Installation (In Progress - Not yet published to maven central)

Add the following dependency to your project.

```kotlin
    debugImplementation("io.rotlabs:flaker-android-okhttp:${latest_version}")
    releaseImplementation("io.rotlabs:flaker-android-okhttp-no-op:${latest_version}")
```

**_NOTE:_** It is not yet published to maven central. But you can download all the modules from [github packages](https://github.com/RotBolt?tab=packages&repo_name=Flaker) and add them to your project for now.

#### flaker-android-okhttp
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

#### flaker-android-ktor (In Progress)

#### flaker-ios-ktor (In Progress)

That's it. Now you can use the companion app to simulate the network conditions.