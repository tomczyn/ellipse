plugins {
    id("android-application-plugin")
}

android.enableCompose()

dependencies {
    implementation(project(":mvi-core"))
    implementation(project(":mvi-compose"))
    kotlin()
    baseAndroid()
    coroutines()
    compose()
    test()
    testImplementation(project(":mvi-test"))
}
