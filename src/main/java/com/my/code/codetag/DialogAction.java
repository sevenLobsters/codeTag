package com.my.code.codetag;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class DialogAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project p = e.getProject();
        String projectName = p.getName();
        String path = p.getProjectFilePath();
        String s2 = p.getBasePath();
        Messages.showMessageDialog(p,s2,projectName,Messages.getInformationIcon());
    }
}
