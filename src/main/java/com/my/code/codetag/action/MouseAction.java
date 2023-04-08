package com.my.code.codetag.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.ui.*;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.UIUtil;
import com.my.code.codetag.CodeTagService;
import com.my.code.codetag.bean.*;
import com.my.code.codetag.ui.SampleDialogWrapper;
import com.my.code.codetag.util.PopUtil;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 *
 */
public class MouseAction extends AnAction {
    JBPopup pop;
    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        Project project = e.getProject();
        DataContext dataContext = e.getDataContext();

        if (project == null) {
            e.getPresentation().setEnabled(false);
        } else {
            System.out.println("has data ");
        }
        e.getPresentation().setText("add tag");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        PsiFile psiFile = e.getData(PlatformDataKeys.PSI_FILE);
        if(file == null || editor == null) return;
        int lineNumber = editor.getCaretModel().getLogicalPosition().line;
        String fileName = file.getName();
        VirtualFile projectBase = LocalFileSystem.getInstance().findFileByPath(project.getBasePath());
        boolean inProject = VfsUtilCore.isAncestor(projectBase, file, true);
        String relativePath = VfsUtilCore.getRelativePath(file, projectBase);
        String fileUrl = file.getUrl();
        VirtualFile createVirtualFile = null;

        List<TagH1> list = CodeTagService.getInstance(project).getStore().getTagH1();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.setUserObject("Content TAG");
        Iterator<TagH1> it = list.iterator();
        while (it.hasNext()){
            TagH1 th1 = it.next();
            DefaultMutableTreeNode t = new DefaultMutableTreeNode();
            t.setUserObject(th1.name);
            List<TagH2> list2 = th1.list;
            Iterator<TagH2> it2 = list2.iterator();
            while (it2.hasNext()){
                TagH2 th2 = it2.next();
                DefaultMutableTreeNode t2 = new DefaultMutableTreeNode();
                t2.setUserObject(th2.name);
                t.add(t2);
            }
            root.add(t);
        }
        // 创建数据模型
        DefaultTreeModel model = new DefaultTreeModel(root);
        Tree tree = new Tree(model);
        tree.setDragEnabled(true);
        tree.setExpandableItemsEnabled(true);


        EditorTextField editorTextField = new EditorTextField();
        editorTextField.setEnabled(true);
        editorTextField.setLayout(new BorderLayout());
        JTextField  jTextField = new JTextField();
        jTextField.setText("enter source code description");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(jTextField,BorderLayout.WEST);
        panel.add(editorTextField,BorderLayout.CENTER);

        JPanel container = new JPanel();
        JBPopupFactory instance = JBPopupFactory.getInstance();
        JButton tConfirm = new JButton("confirm");
        tConfirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Point p = pop.getLocationOnScreen();
                int x = p.x;
                int y = p.y/2;
                //[root, child2, child1Leaf2]
//                System.out.println("select  getSelectionCount "+tree.getSelectionCount()+"  path "+tree.getSelectionPath()+"  len   "+tree.getSelectionPath().toString().split(",").length);
                if(TextUtils.isEmpty(editorTextField.getText())){
                    PopUtil.showTagItem("please input target source code description");
                   return;
                }
                if(tree == null || tree.getSelectionCount() == 0 || ((list != null && list.size() != 0) && (tree.getSelectionPath().toString().split(",").length ==1))){
                    PopUtil.showTagItem("please select a sub tag");
                    return;
                }
                if((list != null && list.size() == 0) && (tree.getSelectionPath().toString().split(",").length ==1)){
                    PopUtil.showTagItem("<html>Please click on the plus icon at the bottom of" +
                                                    "<br>the idea to create a label, and right-click" +
                                                    "<br>he label to create a sub label. </html>");
                    return;
                }
                if((tree.getSelectionPath().toString().split(",").length ==2)){
                    String path = tree.getSelectionPath().toString();
                    path = path.replace("[","");
                    path = path.replace("]","");
                    String[] arrPath = path.split(",");
                    String p1 = arrPath[1];
                    String s = "<html>    Please select a sub label of "+p1+
                               "<br>   to record the source code. (If there " +
                                "<br>   are no sub tags, please right-click" +
                                "<br>   on this tag in the tag list at list at" +
                                "<br>   the bottom of the idea and select the" +
                                "<br>   \"add sub tag\" option to create a sub tag)</html>";
                    PopUtil.showTagItem(s);
                    return;
                }
                String path = tree.getSelectionPath().toString();
                path = path.replace("[","");
                path = path.replace("]","");
                String[] arrPath = path.split(",");
                String p1 = arrPath[1];
                String p2 = arrPath[2];
                String name = editorTextField.getText().trim();
                System.out.println("select  p1 "+p1+"  p2  "+p2+"  name  "+name);
                TagH3 tagH3 = new TagH3();
                tagH3.name = name;
                tagH3.fileName = fileName;
                tagH3.setLineNumber(lineNumber);
                tagH3.setRelativePath(relativePath);
                tagH3.setFileUrl(fileUrl);
                tagH3.setInProject(inProject);
                CodeTagService.getInstance(project).getStore().addTagH3(p1.trim(),p2.trim(),tagH3);
                if(pop != null ){
                    pop.closeOk(e);
                }
            }
        });


        container.setLayout(new BorderLayout());
        container.add(panel,BorderLayout.NORTH);
        container.add(tree,BorderLayout.CENTER);
        container.add(tConfirm,BorderLayout.SOUTH);
        ComponentPopupBuilder builder = instance.createComponentPopupBuilder(container, editorTextField);
        builder.setTitle("add a tag");
        builder.setMinSize(new Dimension(450,400));
        builder.setRequestFocus(true);
        builder.setNormalWindowLevel(true);
        builder.setMovable(true);
        pop = builder.createPopup();
        pop.showInFocusCenter();


    }

    private static class CellRenderer<T> extends SimpleColoredComponent implements ListCellRenderer<T>
    {
        private CellRenderer()
        {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
        {
            clear();
            TagH1 topic = (TagH1) value;
            append(topic.name);

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
