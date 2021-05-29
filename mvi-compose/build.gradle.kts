plugins {
    id("android-library-plugin")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.compose
}

dependencies {
    implementation(project(":mvi-core"))
    kotlin()
    baseAndroid()
    compose()
    coroutines()
}
