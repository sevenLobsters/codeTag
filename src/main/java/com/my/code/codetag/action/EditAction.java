package com.my.code.codetag.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class EditAction extends AnAction {

    public EditAction(){
        super("edit","edit", AllIcons.Actions.Edit);
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("EditAction");
    }
}
