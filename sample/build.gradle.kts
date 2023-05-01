plugins {
    id("android-application-plugin")
}

android {
    namespace = "com.tomcz.sample"
    viewBinding.isEnabled = true
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.composeCompiler
}

dependencies {
    implementation(project(":ellipse-core"))
    kotlin()
    baseAndroid()
    dependencyInjection()
    navigation()
    coroutines()
    compose()
    test()
    testImplementation(project(":ellipse-test"))
}
