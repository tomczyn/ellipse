import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.addPlugin(AndroidConfig.Plugin.androidLib)
        project.addPlugin(AndroidConfig.Plugin.kotlinAndroid)
        project.addPlugin(AndroidConfig.Plugin.kapt)
        project.androidConfiguration { defaultConfig() }
        project.kotlinCompileOptions()
    }
}