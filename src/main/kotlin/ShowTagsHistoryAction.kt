import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ShowTagsHistoryAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        TagsHistoryDialog(GitCommander(e.project), e.project).show()
    }
}