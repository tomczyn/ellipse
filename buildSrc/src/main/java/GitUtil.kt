import org.gradle.api.Project
import java.io.ByteArrayOutputStream

fun Project.getLastTag(): String {
    return runCommand("git", "describe", "--tags", "--abbrev=0")
}

fun Project.runCommand(vararg command: String): String {
    val byteOut = ByteArrayOutputStream()
    exec {
        workingDir = file("./")
        commandLine = command.toList()
        standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}
