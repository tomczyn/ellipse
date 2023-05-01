import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

fun Project.publishEllipse(id: String) {
    afterEvaluate {
        extensions.configure("publishing", Action<PublishingExtension> {
            publications {
                register("release", MavenPublication::class.java) {
                    from(components.findByName("release"))
                    groupId = "com.tomczyn.ellipse"
                    artifactId = id
                    try {
                        version = getLastTag()
                    } catch (e: Throwable) {
                        // no-op
                    }
                }
            }
        })
    }
}
