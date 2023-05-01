plugins {
    id(AndroidConfig.Plugin.kover).version(Versions.kover)
    id(AndroidConfig.Plugin.detekt).version(Versions.detekt)
    id(AndroidConfig.Plugin.ktlint).version(Versions.ktlint)
    id(AndroidConfig.Plugin.gradleNexus).version(Versions.gradleNexus)
}

apply {
    from("scripts/publish-root.gradle")
}

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

detekt {
    toolVersion = Versions.detekt
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
    }
}
