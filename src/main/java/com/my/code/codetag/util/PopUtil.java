package com.my.code.codetag.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.UIUtil;
import com.my.code.codetag.CodeTagService;
import com.my.code.codetag.bean.TagH1;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PopUtil {
    public static String getInputText(String title,String content){
        String inputText =  Messages.showInputDialog(
                title,
                content,
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
        return inputText;
    }

    public static int showTagItem(Project project){
//        List<TagH1> list = CodeTagService.getInstance(project).getStore().getTagH1();
////        int line = editor.getCaretModel().getLogicalPosition().line;
//        Iterator<TagH1> iterator = list.iterator();
//        ArrayList<TagH1> tagList = new ArrayList<>();
//        while (iterator.hasNext()) {
//            TagH1 next = iterator.next();
//            tagList.add(next);
//        }
        List<TagH1> list = CodeTagService.getInstance(project).getStore().getTagH1();
        DefaultListModel<TagH1> h1Model = new DefaultListModel<>();
        h1Model.addAll(list);

        JBList<TagH1> h1List = new JBList<>(h1Model);
        h1List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        h1List.setCellRenderer(new StringRender<TagH1>());
        h1List.setSelectedIndex(0);

//        Messages.showOkCancelDialog(h1List,"select a choice","this is title","ok","cancel",Icons.default_icon);
        return 0;
//        JPanel panel = new JPanel();
//        JTextField jf = new JTextField("hahhahh");
//        panel.add(jf);
//        String[] arr = {"no","yes"};
//        return Messages.showDialog(panel,"please select item","info",arr,1, Icons.default_icon);

    }



    private static class StringRender<BookMarkNode> extends SimpleColoredComponent implements ListCellRenderer<com.my.code.codetag.bean.BookMarkNode>
    {
        private StringRender() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends com.my.code.codetag.bean.BookMarkNode> list, com.my.code.codetag.bean.BookMarkNode value, int index, boolean isSelected, boolean cellHasFocus) {
            clear();

            append(value.name);
            append(
                    " (" + new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date()) + ")",
                    SimpleTextAttributes.GRAY_ATTRIBUTES
            );

            setForeground(UIUtil.getListSelectionForeground(isSelected));
            setBackground(UIUtil.getListSelectionBackground(isSelected));
            return this;
        }
    }

}
