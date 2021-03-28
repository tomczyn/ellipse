buildscript {

    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(Libs.Plugins.BUILD_GRADLE)
        classpath(Libs.Plugins.KOTLIN_GRADLE)
        classpath(Libs.Plugins.JUNIT_5)
        classpath(Libs.Plugins.KTLINT_GRADLE)
    }
}

tasks.register(Tasks.CLEAN, Delete::class) {
    delete(rootProject.buildDir)
}

apply(plugin = Libs.Plugins.KTLINT)
