import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.addPlugin(AndroidConfig.Plugin.ANDROID_LIB)
        project.addPlugin(AndroidConfig.Plugin.KOTLIN_ANDROID)
        project.addPlugin(AndroidConfig.Plugin.KAPT)
        project.kotlinCompileOptions()
        project.kotlinCompileExperimentalCoroutines()
        project.androidConfiguration {
            defaultConfig()
            buildTypesConfig()
        }
    }
}