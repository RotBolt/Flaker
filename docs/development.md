---
hide:
  - navigation
---

## Development

### Library Code Map
![library-code-map.png](assets/library-code-map.png#only-light)
![library-code-map.png](assets/library-code-map-dark.png#only-dark)


### Build
Simply clone this repository in Android Studio Giraffe or above and build the project.

### Module Details

**flaker-domain**: Contains the domain of the Flaker library, used by other modules, including network requests and user preferences. 🏠

**flaker-data**: Contains the data layer of the Flaker library, used by other modules to access locally stored data. All persistent data should be kept here. 📊

**flaker-okhttp-core**: Core module containing the functionality to intercept network requests and simulate network conditions using OkHttp Interceptor. 🌐

**flaker-ktor-core**: Core module containing the functionality to intercept network requests and simulate network conditions using Ktor. 🌐

**flaker-android-ui**: Contains reusable UI elements and components for the Android companion app. 📱🎨

**flaker-android-okhttp**: Contains the companion app that gets installed as a part of the library. This should be used for apps that use OkHttp as their networking library. 📱📡

**flaker-android-ktor**: Contains the companion app that gets installed as a part of the library. This should be used for apps that use Ktor as their networking library. 📱🌐

**flaker-android**: Contains the companion app that gets installed as a part of the library. This should be used for apps that use both OkHttp and Ktor as their networking library. 📱📡🌐

### Verify
We use `detekt` for static code analyis and a job is setup in github actions to run it on every PR. You can run it locally using the following command:
```bash
// For android related changes
./gradlew detekt

// For iOS related changes
./gradlew detektMetadataIosMain

// For common code changes
./gradlew detektMetadataCommonMain
```

If you want to `detekt` to auto correct some of the stuff, please add the flag `--auto-correct` to the above commands.

## Contributing
If you've found an error in this sample, please 🚩 file an issue.

Patches are encouraged and may be submitted by forking this project and submitting a pull request. Since this project is still in its very early stages, if your change is substantial, please raise an issue first to discuss it. 🤝