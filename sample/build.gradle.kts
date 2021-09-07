plugins {
    id("android-application-plugin")
}

android {
    viewBinding.isEnabled = true
}

dependencies {
    implementation(project(":ellipse-core"))
    kotlin()
    dependencyInjection()
    baseAndroid()
    navigation()
    coroutines()
    compose()
    test()
    testImplementation(project(":ellipse-test"))
}
