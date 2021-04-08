plugins {
    id("android-library-plugin")
}

dependencies {
    implementation(project(":mvi-core"))
    implementation(Libs.Jetbrains.Coroutines.Test.coroutines)
    implementation(Libs.JUnit5.jupiter)
    kotlin()
    coroutines()
}