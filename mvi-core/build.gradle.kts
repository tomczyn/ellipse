plugins {
    id("android-library-plugin")
}

dependencies {
    kotlin()
    baseAndroid()
    coroutines()
    test()
    testImplementation(project(":mvi-test"))
}
