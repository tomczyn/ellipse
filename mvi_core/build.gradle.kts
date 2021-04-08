plugins {
    id("android-library-plugin")
}

dependencies {
    kotlin()
    baseAndroid()
    compose()
    coroutines()
    test()
    testImplementation(project(":mvi_test"))
}
