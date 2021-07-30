import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private const val RELEASE = "release"
private const val DEBUG = "debug"

fun Project.kotlinCompileOptions() = tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = AndroidConfig.javaVersion.majorVersion
    }
    kotlinOptions.freeCompilerArgs = kotlinOptions.freeCompilerArgs + listOf(
        "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
    )
}

fun BaseExtension.defaultConfig() {
    compileSdkVersion(AndroidConfig.sdkVersion)
    defaultConfig {
        minSdk = AndroidConfig.minSdkVersion
        targetSdk = AndroidConfig.sdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions()
    buildTypes()
}

fun BaseExtension.enableCompose() {
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

private fun BaseExtension.compileOptions() {
    compileOptions {
        sourceCompatibility = AndroidConfig.javaVersion
        targetCompatibility = AndroidConfig.javaVersion
    }
}

private fun BaseExtension.buildTypes() {
    buildTypes {
        getByName(RELEASE) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName(DEBUG) { }
    }
}

