import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ShowTagsHistoryAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        NotificationGroup("tags-history-plugin", NotificationDisplayType.BALLOON, true)
            .createNotification("Plugin is working", NotificationType.INFORMATION)
            .notify(e.project)
    }
}