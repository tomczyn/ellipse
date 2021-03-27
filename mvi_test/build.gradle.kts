plugins {
    id("android-library-plugin")
}

dependencies {
    implementation(project(":mvi_core"))
    implementation(Libs.Coroutines.TEST)
    implementation(Libs.JUnit5.JUPITER)
    kotlin()
    coroutines()
}