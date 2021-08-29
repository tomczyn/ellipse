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
                version = "0.04"
            }
        }
    }
}

dependencies {
    implementation(project(":ellipse"))
    kotlin()
    coroutines()
    implementation(Libs.JUnit.jupiter)
    implementation(Libs.Jetbrains.Coroutines.Test.coroutines)
}