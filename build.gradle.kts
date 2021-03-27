buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(Libs.Plugins.BUILD_GRADLE)
        classpath(Libs.Plugins.KOTLIN_GRADLE)
        classpath(Libs.Plugins.JUNIT_5)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register(Tasks.CLEAN, Delete::class) {
    delete(rootProject.buildDir)
}