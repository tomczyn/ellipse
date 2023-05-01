plugins {
    id("android-library-plugin")
}

ext["PUBLISH_GROUP_ID"] = "com.tomczyn.ellipse"
ext["PUBLISH_VERSION"] = getLastTag()
ext["PUBLISH_ARTIFACT_ID"] = "ellipse-core"
ext["PUBLISH_DESCRIPTION"] = "Pragmatic Unidirectional Data Flow for Android "
ext["PUBLISH_URL"] = "https://github.com/tomczyn/ellipse"
ext["PUBLISH_LICENSE_NAME"] = "MIT License"
ext["PUBLISH_LICENSE_URL"] = "https://github.com/tomczyn/ellipse/blob/main/LICENSE"
ext["PUBLISH_DEVELOPER_ID"] = "tomczyn"
ext["PUBLISH_DEVELOPER_NAME"] = "Maciej Tomczynski"
ext["PUBLISH_DEVELOPER_EMAIL"] = "dev@tomczyn.com"
ext["PUBLISH_SCM_CONNECTION"] = "scm:git:github.com/tomczyn/ellipse.git"
ext["PUBLISH_SCM_DEVELOPER_CONNECTION"] = "scm:git:ssh://github.com/tomczyn/ellipse.git"
ext["PUBLISH_SCM_URL"] = "https://github.com/tomczyn/ellipse/tree/master"

apply {
    from("${rootProject.projectDir}/scripts/publish-module.gradle")
}

android {
    namespace = "com.tomczyn.ellipse"
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = Versions.composeCompiler
}

dependencies {
    implementation(Libs.Jetbrains.kotlinStdlib)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.vmKtx)
    implementation(Libs.AndroidX.lifecycleRuntimeKtx)
    implementation(Libs.AndroidX.lifecycleRuntimeCompose)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.Jetbrains.Coroutines.android)
    testRuntimeOnly(Libs.JUnit.Runtime.jupiterEngine)
    testImplementation(Libs.JUnit.jupiter)
    testImplementation(Libs.JUnit.jupiterParams)
    testImplementation(Libs.Jetbrains.Coroutines.Test.coroutines)
    testImplementation(project(":ellipse-test"))
}

tasks.koverVerify {
    rule {
        name = "Minimal line coverage rate in percents"
        bound {
            minValue = 35
        }
    }
}
