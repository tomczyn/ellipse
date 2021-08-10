plugins {
    id("android-application-plugin")
}

android {
    viewBinding.isEnabled = true
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.compose
}

dependencies {
    implementation(project(":mvi-core"))
    kotlin()
    dependencyInjection()
    baseAndroid()
    navigation()
    coroutines()
    compose()
    test()
    testImplementation(project(":mvi-test"))
}
