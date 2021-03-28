import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kotlin() {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
}

fun DependencyHandler.baseAndroid() {
    implementation("androidx.core:core-ktx:${Versions.KTX}")
    implementation("androidx.fragment:fragment-ktx:${Versions.KTX}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.KTX_VM}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.KTX_VM}")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
}

fun DependencyHandler.coroutines() {
    implementation(Libs.Coroutines.CORE)
    implementation(Libs.Coroutines.ANDROID)
}

fun DependencyHandler.test() {
    testImplementation(Libs.Coroutines.TEST)
    testImplementation(Libs.JUnit5.JUPITER)
    testImplementation(Libs.JUnit5.JUPITER_PARAMS)
    testRuntimeOnly(Libs.JUnit5.Runtime.JUPITER_ENGINE)
}

fun DependencyHandler.androidTest() {
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
