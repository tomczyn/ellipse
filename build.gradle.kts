buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Libs.Plugins.BUILD_GRADLE)
        classpath(Libs.Plugins.KOTLIN_GRADLE)
        classpath(Libs.Plugins.JUNIT_5)
    }
}

tasks.register(Tasks.CLEAN, Delete::class) {
    delete(rootProject.buildDir)
}