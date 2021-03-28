dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":sample")
include(":mvi_test")
include(":mvi_core")
rootProject.name = "MVI"