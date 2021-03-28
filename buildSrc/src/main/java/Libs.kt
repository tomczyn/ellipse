object Libs {

    object Plugins {
        const val BUILD_GRADLE = "com.android.tools.build:gradle:${Versions.GRADLE}"
        const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
        const val JUNIT_5 =
            "de.mannodermaus.gradle.plugins:android-junit5:${Versions.JUNIT_5_GRADLE}"
        const val KTLINT_GRADLE = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.KTLINT}"
        const val KTLINT = "org.jlleitschuh.gradle.ktlint"
    }

    object AndroidX {
        const val CORE_KTX = "androidx.core:core-ktx:${Versions.KTX}"
        const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Versions.KTX}"
        const val VM_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.KTX_VM}"
        const val LIFECYCLE_RUNTIME_KTX =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.KTX_VM}"
        const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"

        object AndroidTest {
            const val JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_JUNIT}"
            const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
        }
    }

    object Google {
        const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    }

    object Jetbrains {
        const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
        const val CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
        const val ANDROID =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
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