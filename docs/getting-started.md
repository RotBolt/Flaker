# Usage

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

That's it. Now you can use the companion app to simulate the network conditions.