plugins {
    id("android-library-plugin")
    id(AndroidConfig.Plugin.mavenPublish)
    id(AndroidConfig.Plugin.gradleNexus).version(Versions.gradleNexus)
}

publishEllipse("ellipse-core")

android {
    namespace = "com.tomczyn.ellipse"
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.composeCompiler
}

dependencies {
    implementation(Libs.Jetbrains.kotlinStdlib)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.vmKtx)
    implementation(Libs.AndroidX.lifecycleRuntimeKtx)
    implementation(Libs.AndroidX.lifecycleRuntimeCompose)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.Jetbrains.Coroutines.android)
    testRuntimeOnly(Libs.JUnit.Runtime.jupiterEngine)
    testImplementation(Libs.JUnit.jupiter)
    testImplementation(Libs.JUnit.jupiterParams)
    testImplementation(Libs.Jetbrains.Coroutines.Test.coroutines)
    testImplementation(project(":ellipse-test"))
}

tasks.koverVerify {
    rule {
        name = "Minimal line coverage rate in percents"
        bound {
            minValue = 35
        }
    }
}
