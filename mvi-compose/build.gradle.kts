plugins {
    id("android-library-plugin")
}

android.enableCompose()

dependencies {
    implementation(project(":mvi-core"))
    kotlin()
    baseAndroid()
    compose()
    coroutines()
}
