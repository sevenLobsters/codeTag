package com.my.code.codetag.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.my.code.codetag.App;
import com.my.code.codetag.CodeTagService;
import com.my.code.codetag.util.TagUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportAction extends AnAction {

    public ExportAction(){
        super("export","export", AllIcons.ToolbarDecorator.Export);
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("ExportAction");
        Project project = e.getProject();
        CodeTagService service = CodeTagService.getInstance(project);
        FileSaverDescriptor fsd = new FileSaverDescriptor("Save", "Please choose where to save", "xml");

        VirtualFile baseDir;
        baseDir = LocalFileSystem.getInstance().findFileByPath(System.getProperty("user.home"));
        System.out.println("save dir is "+baseDir);
        final VirtualFileWrapper wrapper = FileChooserFactory.getInstance().
                createSaveFileDialog(fsd, project).
                save(
                        baseDir,
                        App.name + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xml"
                );

        if (wrapper == null) {
            return;
        }

        File file = wrapper.getFile();

        File parentDir = file.getParentFile();
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Messages.showErrorDialog(project, "Fail to save. Please try again.", App.name + "Save");
            return;
        }

        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        Element state = TagUtil.buildElement(service.getStore().getTagH1().iterator());
        try {
            xmlOutput.output(new Document(state), fileOutputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
            Messages.showErrorDialog(project, "Fail to save. Please try again.", App.name + "Save");
            return;
        }

    }
}
