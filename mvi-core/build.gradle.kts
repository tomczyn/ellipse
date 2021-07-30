plugins {
    id("android-library-plugin")
    id(AndroidConfig.Plugin.mavenPublish)
}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components.findByName("release"))
                groupId = "com.tomcz.mvi.mvi-core"
                artifactId = "mvi-core"
                version = "0.0.6"
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
    testImplementation(project(":mvi-test"))
}
