plugins {
    id("android-library-plugin")
}

dependencies {
    implementation(project(":mvi-core"))
    kotlin()
    coroutines()
    implementation(Libs.JUnit.jupiter)
    implementation(Libs.Jetbrains.Coroutines.Test.coroutines)
}