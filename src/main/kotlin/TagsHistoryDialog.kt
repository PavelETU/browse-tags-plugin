import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.util.Consumer
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.io.File
import javax.swing.*

class TagsHistoryDialog(private val gitCommander: GitCommander, private val project: Project?) : DialogWrapper(true) {
    init {
        init()
        setResizable(false)
        title = "History of Tags"
    }

    private lateinit var outputText: JBTextArea

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        val listOfTags = gitCommander.getTags()
        val list = JBList(listOfTags)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.layoutOrientation = JList.VERTICAL
        val scrollForList = JBScrollPane(list)
        panel.add(scrollForList, BorderLayout.LINE_START)
        outputText =
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

    override fun createActions(): Array<Action> = arrayOf(object : DialogWrapperAction("Select File To Display") {
        override fun doAction(e: ActionEvent?) {
            FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), project, project!!.baseDir)
            { t -> outputText.append("\n${t?.path ?: "No file chosen"}") }
        }
    }, object : DialogWrapperAction("Close") {
        override fun doAction(e: ActionEvent?) {
            doOKAction()
        }
    })
}