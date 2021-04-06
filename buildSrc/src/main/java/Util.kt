
import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.addPlugin(name: String) {
    plugins.apply(name)
}

fun Project.androidConfiguration(block: BaseExtension.() -> Unit) =
    (project.extensions.getByName("android") as BaseExtension).apply(block)

fun Project.kotlinCompileOptions() = tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

fun Project.kotlinCompileExperimentalCoroutines() = tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs + listOf(
        "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
    )
}

fun Project.kotlinIRBackend() = tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        useIR = true
    }
}

fun BaseExtension.defaultConfig() {
    compileSdkVersion(AndroidConfig.SDK_VERSION)
    compileOptions {
        sourceCompatibility = AndroidConfig.JVM_TARGET
        targetCompatibility = AndroidConfig.JVM_TARGET
    }
    defaultConfig {
        minSdkVersion(AndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfig.SDK_VERSION)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

fun BaseExtension.compose() {
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }
}

fun DependencyHandler.kaptAndroidTest(dependency: String) {
    add("kaptAndroidTest", dependency)
}

fun DependencyHandler.androidTestImplementation(dependency: String) {
    add("androidTestImplementation", dependency)
}

fun DependencyHandler.testImplementation(dependency: String) {
    add("testImplementation", dependency)
}

fun DependencyHandler.testRuntimeOnly(dependency: String) {
    add("testRuntimeOnly", dependency)
}

fun DependencyHandler.implementation(dependency: String) {
    add("implementation", dependency)
}

fun DependencyHandler.debugImplementation(dependency: String) {
    add("debugImplementation", dependency)
}

fun DependencyHandler.kapt(dependency: String) {
    add("kapt", dependency)
}
