package com.my.code.codetag.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.UIUtil;
import com.my.code.codetag.CodeTagService;
import com.my.code.codetag.bean.TagH1;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
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

    public static void showTagItem(String text){
        JPanel container = new JPanel();
        JBPopupFactory instance = JBPopupFactory.getInstance();
        JButton tConfirm = new JButton("confirm");
        tConfirm.setMaximumSize(new Dimension(150,50));
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        container.setLayout(new BorderLayout());
        container.add(label,BorderLayout.CENTER);
        container.add(tConfirm,BorderLayout.SOUTH);
        ComponentPopupBuilder builder = instance.createComponentPopupBuilder(container, tConfirm);
        builder.setTitle("help");
        builder.setMinSize(new Dimension(250,200));
        builder.setRequestFocus(true);
        builder.setNormalWindowLevel(true);
        builder.setMovable(true);
        JBPopup pop = builder.createPopup();
        pop.showInFocusCenter();
        tConfirm.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(pop != null && pop.isVisible()){
                    pop.cancel();
                }
            }
        });
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
