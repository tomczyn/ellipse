plugins {
    id("android-library-plugin")
    id(AndroidConfig.Plugin.mavenPublish)
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components.findByName("release"))
                groupId = "com.tomcz.mvi.test"
                artifactId = "mvi-test"
                version = "0.03"
            }
        }
    }
}

dependencies {
    implementation(project(":mvi-core"))
    kotlin()
    coroutines()
    implementation(Libs.JUnit.jupiter)
    implementation(Libs.Jetbrains.Coroutines.Test.coroutines)
}