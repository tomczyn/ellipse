plugins {
    id("android-library-plugin")
}

dependencies {
    implementation(project(":mvi_core"))
    implementation(Libs.Jetbrains.TEST)
    implementation(Libs.JUnit5.JUPITER)
    kotlin()
    coroutines()
}