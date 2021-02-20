import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ScriptRunnerUtil
import com.intellij.openapi.project.Project
import java.io.File
import java.nio.charset.Charset

class GitCommander(private val project: Project?) {
    fun getTags(): List<String> {
        val commands = listOf("git", "tag", "--list")
        val generalCommandLine = GeneralCommandLine(commands)
        generalCommandLine.charset = Charset.forName("UTF-8")
        generalCommandLine.workDirectory = File(project?.basePath ?: project.toString())
        return ScriptRunnerUtil.getProcessOutput(generalCommandLine).split("\n").dropLastWhile { last -> last.isEmpty() }
    }

    fun getInfoForATag(tag: String): String {
        val commands = listOf("git", "show", "-s", "--format=%ci", "tags/$tag")
        val generalCommandLine = GeneralCommandLine(commands)
        generalCommandLine.charset = Charset.forName("UTF-8")
        generalCommandLine.workDirectory = File(project?.basePath ?: project.toString())
        return ScriptRunnerUtil.getProcessOutput(generalCommandLine)
    }
}