import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*

class TagsHistoryDialog(private val gitCommander: GitCommander, private val project: Project?) : DialogWrapper(true) {
    init {
        init()
        setResizable(false)
        title = "History of Tags"
    }

    private lateinit var outputText: JBTextArea
    private var tag: String? = null
    private var filePath: String? = null
    private var fileName: String? = null

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
                tag = list.selectedValue
                buildOutputTextForTag()
            }
        }
        return panel
    }

    override fun createActions(): Array<Action> = arrayOf(object : DialogWrapperAction("Select File To Display") {
        override fun doAction(e: ActionEvent?) {
            FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor(), project, project!!.baseDir)
            { t -> filePath = t.path; fileName = t.name; buildOutputText() }
        }
    }, object : DialogWrapperAction("Close") {
        override fun doAction(e: ActionEvent?) {
            doOKAction()
        }
    })

    private fun buildOutputText() {
        if (tag == null) buildGeneralOutputText()
        else buildOutputTextForTag()
    }

    private fun buildGeneralOutputText() {
        fileName ?: return
        outputText.text = "The context of $fileName will be displayed here\nwith a general info about the tag\n\nTo choose a tag select one of the versions"
    }

    private fun buildOutputTextForTag() {
        val generalTagInfo = "$tag \n\nDev notes\\release Date:\n\n${gitCommander.getInfoForATag(tag!!)}\n"
        var somethingToAppend = ""
        if (filePath != null) {
            val fileContent = gitCommander.getFileForTag(filePath!!, tag!!)
            somethingToAppend = if (fileContent.isEmpty()) {
                "There is no $fileName for $tag tag"
            } else {
                "Content of $fileName file:\n\n$fileContent\n"
            }
        }
        outputText.text = generalTagInfo.plus(somethingToAppend)
    }
}