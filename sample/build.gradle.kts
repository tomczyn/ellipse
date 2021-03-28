plugins {
    id("android-application-plugin")
}

dependencies {
    implementation(project(":mvi_core"))
    kotlin()
    baseAndroid()
    coroutines()
}
