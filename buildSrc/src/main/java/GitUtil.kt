import org.gradle.api.Project
import java.io.ByteArrayOutputStream

fun Project.getLastTag(): String {
    return runCommand("git describe --tags --abbrev=0")
}

fun Project.runCommand(command: String): String {
    val byteOut = ByteArrayOutputStream()
    exec {
        workingDir = file("./")
        commandLine = command.split("\\s".toRegex())
        standardOutput = byteOut
    }
    return String(byteOut.toByteArray()).trim()
}
