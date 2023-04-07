package com.my.code.codetag.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import com.my.code.codetag.App;
import com.my.code.codetag.CodeTagService;
import com.my.code.codetag.bean.TagStore;
import com.my.code.codetag.util.TagUtil;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static com.my.code.codetag.bean.TagStore.H1Change.TAG_H1;

public class ImportAction extends AnAction {

    public ImportAction(){
        super("Import", "Import", AllIcons.ToolbarDecorator.Import);
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("ImportAction");
        Project project = e.getProject();
        CodeTagService service = CodeTagService.getInstance(project);

        VirtualFile baseDir;
        baseDir = LocalFileSystem.getInstance().findFileByPath(System.getProperty("user.home"));
        FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("xml");
        VirtualFile[] files = FileChooserFactory.getInstance().
                createFileChooser(fileChooserDescriptor, project, null).
                choose(project, baseDir);

        if (files.length == 0) {
            return;
        }

        VirtualFile parentDir = files[0].getParent();
        System.out.println("ImportAction path is  "+parentDir.getPath());
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try {
            document = builder.build(new File(files[0].getPath()));
        } catch (JDOMException ex) {
            Messages.showErrorDialog(project, "Fail to load action caused by illegal format file content.", App.name + "Load");
        } catch (IOException ex) {
            Messages.showErrorDialog(project, "Fail to load action. Please try again.", App.name + "Load");
            return;
        }
        if (document == null) {
            return;
        }
        try {
            service.getStore().setRootTag(TagUtil.readData(document.getRootElement()));
            MessageBus messageBus = project.getMessageBus();
            TagStore.H1Change publisher = messageBus.syncPublisher(TAG_H1);
            publisher.loadTagH1();
        } catch (Exception e2) {
            Messages.showErrorDialog(
                    project,
                    "Fail to load action caused by illegal format file content.",
                    App.name + "Load"
            );
        }

    }
}
