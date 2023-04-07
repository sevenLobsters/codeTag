package com.my.code.codetag.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class RemoveAction extends AnAction {

    public RemoveAction(){
        super("remove","remove", AllIcons.General.Remove);
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {


        System.out.println("RemoveAction");
    }
}
