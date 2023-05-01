plugins {
    id("android-library-plugin")
    id(AndroidConfig.Plugin.mavenPublish)
}

ext["PUBLISH_GROUP_ID"] = "com.tomczyn.ellipse"
ext["PUBLISH_VERSION"] = getLastTag()
ext["PUBLISH_ARTIFACT_ID"] = "ellipse-test"
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
    namespace = "com.tomczyn.ellipse.test"
}

dependencies {
    implementation(project(":ellipse-core"))
    implementation(Libs.Jetbrains.kotlinStdlib)
    implementation(Libs.Jetbrains.Coroutines.android)
    implementation(Libs.JUnit.jupiter)
    implementation(Libs.Jetbrains.Coroutines.Test.coroutines)
}
