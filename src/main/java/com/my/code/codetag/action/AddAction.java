package com.my.code.codetag.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.messages.MessageBus;
import com.my.code.codetag.CodeTagService;
import com.my.code.codetag.bean.TagH1;
import com.my.code.codetag.bean.TagStore;
import com.my.code.codetag.util.PopUtil;
import com.my.code.codetag.util.TagUtil;
import org.jetbrains.annotations.NotNull;

import static com.my.code.codetag.bean.TagStore.H1Change.TAG_H1;

/**
 * 这里增加一个弹窗
 *
 */
public class AddAction extends AnAction {

    public AddAction(){
        super("add new tag", "add",AllIcons.General.Add);
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String tagName =  Messages.showInputDialog(
                "Enter tag name",
                "Create New tag",
                Messages.getQuestionIcon(),
                "",
                new InputValidator()
                {
                    @Override
                    public boolean checkInput(String inputString) { return !inputString.trim().equals(""); }

                    @Override
                    public boolean canClose(String inputString) { return true; }
                }
        );

        if (tagName != null) {
            CodeTagService service = CodeTagService.getInstance(e.getProject());
            if(TagUtil.hadSameTagH1(e.getProject(),tagName)){
                Messages.showErrorDialog("'"+tagName+"'"+" has been used ","tag name error");
            } else {
                service.getStore().addTagH1(new TagH1(tagName));
                MessageBus messageBus = e.getProject().getMessageBus();
                TagStore.H1Change publisher = messageBus.syncPublisher(TAG_H1);
                publisher.loadTagH1();
            }
        }
        System.out.println("AddAction");
    }
}
