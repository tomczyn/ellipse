plugins {
    id("android-library-plugin")
    id(AndroidConfig.Plugin.mavenPublish)
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components.findByName("release"))
                groupId = "com.tomcz.ellipse.test"
                artifactId = "ellipse-test"
                version = "0.07"
            }
        }
    }
}

dependencies {
    implementation(project(":ellipse-core"))
    implementation(Libs.Jetbrains.kotlinStdlib)
    implementation(Libs.Jetbrains.Coroutines.android)
    implementation(Libs.JUnit.jupiter)
    implementation(Libs.Jetbrains.Coroutines.Test.coroutines)
}