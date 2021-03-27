import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.kotlin() {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
}

fun DependencyHandler.baseAndroid() {
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")
}

fun DependencyHandler.coroutines() {
    implementation(Libs.Coroutines.CORE)
    implementation(Libs.Coroutines.ANDROID)
}

fun DependencyHandler.test() {
    testImplementation(Libs.JUnit5.JUPITER)
    testImplementation(Libs.JUnit5.JUPITER_PARAMS)
    testRuntimeOnly(Libs.JUnit5.Runtime.JUPITER_ENGINE)
}

fun DependencyHandler.androidTest() {
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
