import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kotlin() {
    implementation(Libs.Jetbrains.kotlinStdlib)
}

fun DependencyHandler.baseAndroid() {
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.fragmentKtx)
    implementation(Libs.AndroidX.vmKtx)
    implementation(Libs.AndroidX.lifecycleRuntimeKtx)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.Google.material)
}

fun DependencyHandler.compose() {
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.uiTooling)
    implementation(Libs.AndroidX.Compose.foundation)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.constraint)
    implementation(Libs.AndroidX.Compose.materialIconsCore)
    implementation(Libs.AndroidX.Compose.materialIconsExtended)
    implementation(Libs.AndroidX.Compose.activity)
    implementation(Libs.AndroidX.Compose.viewModel)
    androidTestImplementation(Libs.AndroidX.Compose.AndroidTest.uiTest)
}

fun DependencyHandler.coroutines() {
    implementation(Libs.Jetbrains.Coroutines.core)
    implementation(Libs.Jetbrains.Coroutines.android)
}

fun DependencyHandler.test() {
    testImplementation(Libs.Jetbrains.Coroutines.Test.coroutines)
    testImplementation(Libs.JUnit.jupiter)
    testImplementation(Libs.JUnit.jupiterParams)
    testRuntimeOnly(Libs.JUnit.Runtime.jupiterEngine)
}

fun DependencyHandler.androidTest() {
    androidTestImplementation(Libs.AndroidX.AndroidTest.junit)
    androidTestImplementation(Libs.AndroidX.AndroidTest.espresso)
}
