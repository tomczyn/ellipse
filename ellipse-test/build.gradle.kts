plugins {
    id("android-library-plugin")
    id(AndroidConfig.Plugin.mavenPublish)
}

publishEllipse("ellipse-test")

dependencies {
    implementation(project(":ellipse-core"))
    implementation(Libs.Jetbrains.kotlinStdlib)
    implementation(Libs.Jetbrains.Coroutines.android)
    implementation(Libs.JUnit.jupiter)
    implementation(Libs.Jetbrains.Coroutines.Test.coroutines)
}