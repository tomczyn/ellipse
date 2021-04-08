import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinLibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.addPlugin(AndroidConfig.Plugin.JAVA_LIB)
        project.addPlugin(AndroidConfig.Plugin.KOTLIN)
        project.kotlinCompileOptions()
    }
}
