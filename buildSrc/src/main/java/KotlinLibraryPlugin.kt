import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.addPlugin(AndroidConfig.Plugin.javaLib)
        project.addPlugin(AndroidConfig.Plugin.kotlin)
        project.kotlinCompileOptions()
    }
}
