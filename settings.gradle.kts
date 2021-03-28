dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
    }
}

rootProject.name = "MVI"

include(":sample")
include(":mvi_test")
include(":mvi_core")
