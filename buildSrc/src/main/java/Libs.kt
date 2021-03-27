object Libs {
    object Plugins {
        const val BUILD_GRADLE = "com.android.tools.build:gradle:${Versions.GRADLE}"
        const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
        const val JUNIT_5 =
            "de.mannodermaus.gradle.plugins:android-junit5:${Versions.JUNIT_5_GRADLE}"
    }

    object Coroutines {
        const val CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
        const val ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
        const val TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
    }

    object JUnit5 {

        const val JUPITER = "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT_5}"
        const val JUPITER_PARAMS = "org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT_5}"

        object Runtime {
            const val JUPITER_ENGINE = "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT_5}"
        }
    }
}