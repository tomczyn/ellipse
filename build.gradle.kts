buildscript {

    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(Libs.Plugins.buildGradle)
        classpath(Libs.Plugins.kotlinGradle)
        classpath(Libs.Plugins.junit5)
        classpath(Libs.Plugins.ktlintGradle)
        classpath(Libs.Plugins.hilt)
        classpath(Libs.Plugins.safeArgs)
    }
}

tasks.register(Tasks.clean, Delete::class) {
    delete(rootProject.buildDir)
}

apply(plugin = AndroidConfig.Plugin.ktlint)
