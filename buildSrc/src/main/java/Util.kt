import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

const val RES_STRING = "string"
const val CONFIG_STRING = "String"
const val CONFIG_BOOL = "Boolean"
const val CONFIG_INT = "int"

fun Project.addPlugin(name: String) {
    plugins.apply(name)
}

fun Project.androidConfiguration(block: BaseExtension.() -> Unit) {
    val android = project.extensions.getByName("android")
    if (android is BaseExtension) android.apply(block)
}

fun Project.kotlinCompileOptions() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = AndroidConfig.JVM_TARGET
    }
}

fun Project.kotlinCompileExperimentalCoroutines() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs + listOf(
            "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
        )
    }
}

fun BaseExtension.defaultConfig() {
    compileSdkVersion(AndroidConfig.SDK_VERSION)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    defaultConfig {
        minSdkVersion(AndroidConfig.MIND_SDK_VERSION)
        targetSdkVersion(AndroidConfig.SDK_VERSION)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
