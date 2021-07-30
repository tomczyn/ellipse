[![](https://jitpack.io/v/MTomczynski/MVI.svg)](https://jitpack.io/#MTomczynski/MVI)

## 🚧 MVI - Unidirectional Data Flow for Android 🚧
Work in progress 🏗️


MVI is a library that helps to implement unidirectional data flow in [Kotlin](https://github.com/jetbrains/kotlin) using [Coroutines](https://github.com/Kotlin/kotlinx.coroutines). It follows principle of preferring composition over inheritance, all API's are based on extension functions. Thanks to this design choice library plays well with [Jetpack Compose](https://developer.android.com/jetpack/compose) or [Dagger/Dagger Hilt](https://dagger.dev/).

### How to use it
Add it in your root build.gradle.kts at the end of repositories:
```kotlin
allprojects {
  repositories {
    maven("https://jitpack.io")
  }
}
```

Add the dependency
```kotlin
dependencies {
    implementation("com.github.MTomczynski.MVI:mvi-core:0.01")
    testImplementation("com.github.MTomczynski.MVI:mvi-test:0.01")
}
```
