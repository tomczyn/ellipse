plugins {
    id("android-application-plugin")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.compose
}

dependencies {
    implementation(project(":mvi-core"))
    implementation(project(":mvi-compose"))
    kotlin()
    baseAndroid()
    coroutines()
    compose()
    test()
    testImplementation(project(":mvi-test"))
}
