plugins {
    id("android-library-plugin")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.compose
}

dependencies {
    kotlin()
    baseAndroid()
    compose()
    coroutines()
    test()
    testImplementation(project(":mvi-test"))
}
