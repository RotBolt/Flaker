# :construction: Flaker :construction:
![flaker build](https://github.com/rotbolt/flaker/actions/workflows/flaker-ci.yml/badge.svg) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.rotbolt/flaker-android-okhttp/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/io.github.rotbolt/flaker-android-okhttp)

Developing a mobile app that performs flawlessly in the real world requires thorough testing under a variety of network conditions. Introducing Flaker – your assistant tool for recreating real-world network scenarios directly in your mobile app development environment. 📱💡

## What is Flaker?
Flaker is a network simulator designed to make your mobile app development process smoother and more efficient. With Flaker, you can effortlessly emulate a wide range of network conditions, enabling you to fine-tune your app's performance and ensure it delivers an exceptional user experience under any circumstance. 🌐🛠️

## Key Features:
### Experience Slow Networks 🐢
Ever wondered how your app would perform on a slow and sluggish network? With Flaker, you can replicate these scenarios with ease. Test your app's responsiveness and optimize its behavior under adverse network conditions. 🚀🐌

### Emulate Flaky Networks 📶
Network connections in the real world are rarely stable. Flaker allows you to introduce fail percentage and variance controls, mirroring the unpredictable nature of network connections. Gain a firsthand understanding of the challenges your mobile app might face and ensure it remains resilient. 🔮🔌

### Focused Impact 🎯
Flaker is designed to impact only your app's network conditions, leaving your device's overall network functionality undisturbed. This means you can test and fine-tune your app's performance without affecting your entire device's connectivity. 📡🔍

## Who Can Benefit from Flaker?
Whether you're a mobile app developer looking to enhance user experiences or a tester seeking to validate your app's resilience, Flaker provides an intuitive and indispensable tool within your development workflow. Elevate your testing strategies and boost your app's performance with Flaker.

Flaker empowers you to:

✅ Test under various network conditions

✅ Optimize your app for slow networks

✅ Prepare your app for flaky network connections

✅ Ensure your app's stability and reliability

✅ Enhance user satisfaction and retention

## Demo
📺 Check out the to see Flaker in action.

https://github.com/RotBolt/Flaker/assets/24780524/3d00e644-0f47-4755-8402-74001fa96a2c

## Usage
Please refer to the [📚 docs](https://rotbolt.github.io/Flaker/) for detailed usage instructions.

## Built with

**Kotlin:** 🚀 The primary programming language.

**Kotlin Multiplatform:** 📱🍏 Sharing common logic across Android and iOS.

**Jetpack Compose**: 🖼️ For building the UI for the Android companion app. Supports dynamic theming.

**SqlDelight:** 📊 For a shared database and persistence layer between multiple platforms.

**Jetpack DataStore:** 📦 For the shared persistence layer to store user preferences and configuration of Flaker.

**Okhttp:** 📡 For creating Flaker for Android apps using Okhttp3 for networking.

**Ktor:** 🌐 For creating Flaker targeting both Android and iOS apps using Ktor for networking. (🚧 In Progress)

**SwiftUI:** 🍎 For building the UI for the iOS companion app. Supports dynamic theming. (🚧 In Progress)

## Roadmap
- [x] flaker-android-okhttp
- [ ] flaker-android-ktor
- [ ] flaker-ios-ktor

## Contributing
If you've found an error in this sample, please 🚩 file an issue.

Patches are encouraged and may be submitted by forking this project and submitting a pull request. Since this project is still in its very early stages, if your change is substantial, please raise an issue first to discuss it. 🤝

## License
```
Copyright 2023 Rohan Maity

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
