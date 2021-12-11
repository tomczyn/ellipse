import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.addPlugin(AndroidConfig.Plugin.androidLib)
        project.addPlugin(AndroidConfig.Plugin.junit5)
        project.addPlugin(AndroidConfig.Plugin.kotlinAndroid)
        project.addPlugin(AndroidConfig.Plugin.kapt)
        project.addPlugin(AndroidConfig.Plugin.ktlint)
        project.addPlugin(AndroidConfig.Plugin.detekt)
        project.androidConfiguration { defaultConfig() }
        project.kotlinCompileOptions()
    }
}