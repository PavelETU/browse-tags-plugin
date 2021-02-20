import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*

class TagsHistoryDialog(private val gitCommander: GitCommander): DialogWrapper(true) {
    init {
        init()
        setResizable(false)
        title = "History of Tags"
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        val listOfTags = gitCommander.getTags()
        val list = JBList(listOfTags)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.layoutOrientation = JList.VERTICAL
        val scrollForList = JBScrollPane(list)
        panel.add(scrollForList, BorderLayout.LINE_START)
        val outputText =
                JBTextArea("The context of a tag will be displayed here\n\nTo choose a tag select one of the versions")
        outputText.isEditable = false
        val scrollPaneForText = JBScrollPane(outputText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS)
        scrollPaneForText.preferredSize = Dimension(500, 250)
        panel.add(scrollPaneForText, BorderLayout.LINE_END)
        list.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                val tag = list.selectedValue
                outputText.text = "$tag \n\nDev notes\\release Date:\n\n${gitCommander.getInfoForATag(tag)}\n"
            }
        }
        return panel
    }

    override fun createActions() = arrayOf(okAction)
}