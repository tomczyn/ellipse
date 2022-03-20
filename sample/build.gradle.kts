plugins {
    id("android-application-plugin")
}

android {
    viewBinding.isEnabled = true
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.compose
}

dependencies {
    implementation(project(":ellipse-core"))
    kotlin()
    baseAndroid()
    navigation()
    coroutines()
    compose()
    test()
    testImplementation(project(":ellipse-test"))
}
