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
    }
}

tasks.register(Tasks.clean, Delete::class) {
    delete(rootProject.buildDir)
}

apply(plugin = Libs.Plugins.ktlint)
