package com.my.code.codetag.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class BookMarkFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        BookMarkPanel bmp = new BookMarkPanel(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(bmp, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
