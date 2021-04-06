import org.gradle.api.JavaVersion

object AndroidConfig {

    object Plugin {
        const val ANDROID_APP = "com.android.application"
        const val ANDROID_LIB = "com.android.library"
        const val KOTLIN_ANDROID = "kotlin-android"
        const val KAPT = "kotlin-kapt"
        const val JAVA_LIB = "java-library"
        const val KOTLIN = "kotlin"
    }

    const val SDK_VERSION = 30
    const val MIN_SDK_VERSION = 30
    val JVM_TARGET = JavaVersion.VERSION_1_8
}
