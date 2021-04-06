import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kotlin() {
    implementation(Libs.Jetbrains.KOTLIN_STDLIB)
}

fun DependencyHandler.baseAndroid() {
    implementation(Libs.AndroidX.CORE_KTX)
    implementation(Libs.AndroidX.FRAGMENT_KTX)
    implementation(Libs.AndroidX.VM_KTX)
    implementation(Libs.AndroidX.LIFECYCLE_RUNTIME_KTX)
    implementation(Libs.AndroidX.APP_COMPAT)
    implementation(Libs.Google.MATERIAL)
}

fun DependencyHandler.compose() {
    implementation(Libs.AndroidX.Compose.UI)
    implementation(Libs.AndroidX.Compose.UI_TOOLING)
    implementation(Libs.AndroidX.Compose.FOUNDATION)
    implementation(Libs.AndroidX.Compose.MATERIAL)
    implementation(Libs.AndroidX.Compose.CONSTRAINT)
    implementation(Libs.AndroidX.Compose.MATERIAL_ICONS_CORE)
    implementation(Libs.AndroidX.Compose.MATERIAL_ICONS_EXTENDED)
    implementation(Libs.AndroidX.Compose.ACTIVITY)
    implementation(Libs.AndroidX.Compose.VM)
    androidTestImplementation(Libs.AndroidX.Compose.AndroidTest.UI_TESTS)
}

fun DependencyHandler.coroutines() {
    implementation(Libs.Jetbrains.CORE)
    implementation(Libs.Jetbrains.ANDROID)
}

fun DependencyHandler.test() {
    testImplementation(Libs.Jetbrains.TEST)
    testImplementation(Libs.JUnit5.JUPITER)
    testImplementation(Libs.JUnit5.JUPITER_PARAMS)
    testRuntimeOnly(Libs.JUnit5.Runtime.JUPITER_ENGINE)
}

fun DependencyHandler.androidTest() {
    androidTestImplementation(Libs.AndroidX.AndroidTest.JUNIT)
    androidTestImplementation(Libs.AndroidX.AndroidTest.ESPRESSO)
}
