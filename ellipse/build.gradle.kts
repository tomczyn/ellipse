plugins {
    id("android-library-plugin")
    id(AndroidConfig.Plugin.mavenPublish)
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components.findByName("release"))
                groupId = "com.tomcz.ellipse"
                artifactId = "ellipse"
                version = "0.06"
            }
        }
    }
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.compose
}

dependencies {
    kotlin()
    baseAndroid()
    compose()
    coroutines()
    test()
    testImplementation(project(":ellipse-test"))
}
