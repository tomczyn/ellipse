dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://jitpack.io")
    }
}

rootProject.name = "MVI"

include(":sample")
include(":mvi-test")
include(":mvi-core")
