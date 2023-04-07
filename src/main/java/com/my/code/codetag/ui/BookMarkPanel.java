package com.my.code.codetag.ui;

import com.intellij.icons.AllIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.popup.util.DetailViewImpl;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.my.code.codetag.App;
import com.my.code.codetag.CodeTagService;
import com.my.code.codetag.action.*;
import com.my.code.codetag.bean.*;
import com.my.code.codetag.util.BUtils;
import com.my.code.codetag.util.PopUtil;
import com.my.code.codetag.util.TagUtil;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import jdk.javadoc.internal.doclets.formats.html.markup.TagName;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.my.code.codetag.bean.TagStore.H1Change.TAG_H1;

public class BookMarkPanel extends JPanel {
    private Project project;
    private EditorTextField edit;
    private DefaultListModel<TagH3> h3Model;
    private JBList<TagH3> h3List;
    private CodeDetailView codeDetailView;
    private CodeTagService service;
    private String selectTag = "";
    private String selectTagChild = "";
    private Tree tree;
    private DefaultTreeModel treeModel;

    public BookMarkPanel(Project project){
        super(new BorderLayout());
        this.project = project;
        if(project == null) return;
        service = CodeTagService.getInstance(project);
        //动作图标
        add(getBookMarkMenu(), BorderLayout.PAGE_START);

        JBSplitter splitPane = new JBSplitter(0.15f);
        splitPane.setHonorComponentsMinimumSize(false);
        //大标题
        System.out.println("");
        splitPane.setFirstComponent(new JBScrollPane(getTreeList()));
        splitPane.setSecondComponent(getSecondPanel());
        add(splitPane);


        MessageBus messageBus = project.getMessageBus();

        messageBus.connect().subscribe(TAG_H1, new TagStore.H1Change() {
            @Override
            public void onAddTagH1(TagH1 tagH1) {
                System.out.print("  onAddTagH1   "+tagH1.name);
//                h1Model.addElement(tagH1);
            }

            @Override
            public void onRemoveTagH1(TagH1 tagH1) {
                System.out.print("  onRemoveTagH1   "+tagH1.name);
//                h1Model.removeElement(tagH1);
            }

            @Override
            public void loadTagH1() {
                System.out.print("  loadTagH1  do reload   ");
                reloadData();
            }
        });


        messageBus.connect().subscribe(TagStore.H3Change.TAG_H3, new TagStore.H3Change() {
            @Override
            public void onAddTagH3(TagH2 parent, TagH3 tagH) {

            }

            @Override
            public void onAddTagH3ByName(String t1, String t2, TagH3 h3) {

                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) findTreeNodeByName(t1,t2,"");
//                System.out.println("t1 "+t1+"  t2 "+t2+" h3 "+h3+" addNode != null "+(parent != null));
                if(parent != null){
                    TreeNode[] arr = treeModel.getPathToRoot(parent);
                    TreePath path = new TreePath(arr);
//                    System.out.println(" add tagh3 path  "+path.toString());
                    tree.scrollPathToVisible(path);
                    tree.setSelectionPath(path);
                    handleSelectPath();
                }


            }

            @Override
            public void onRemoveTagH3(TagH2 parent, TagH3 tagH) {

            }
        });

    }

    public TreeNode findTreeNodeByName(String t1,String t2,String tagName){
        System.out.println("findTreeNodeByName t1= "+t1+"t2= "+t2+" tagNme="+tagName);
        if(treeModel != null){
            TreeNode root = (TreeNode) treeModel.getRoot();
            Enumeration en = root.children();
            while (en.hasMoreElements()){
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
                String h1 = node.getUserObject().toString().trim();
                System.out.println("findTreeNodeByName h1 "+h1);
                if(h1.equals(t1)){
                    if(TextUtils.isEmpty(t2)){
                        return node;
                    }
                    Enumeration en2 = node.children();
                    while (en2.hasMoreElements()){
                        DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) en2.nextElement();
                        String h2 = node2.getUserObject().toString().trim();
                        System.out.println("findTreeNodeByName h2 "+h2);
                        if(h2.equals(t2)){
                            if(TextUtils.isEmpty(tagName)){
                                return node2;
                            }
                            Enumeration en3 = node2.children();
                            while (en3.hasMoreElements()){
                                DefaultMutableTreeNode node3 = (DefaultMutableTreeNode) en2.nextElement();
                                String h3 = node2.getUserObject().toString().trim();
                                System.out.println("findTreeNodeByName h3 "+h3);
                                if(h3.equals(tagName)){
                                    return node3;
                                }
                            }
                        }
                    }

                    return node;
                }

            }
        }
        return null;
    }

    private void reloadData() {
        if(treeModel != null){
            treeModel.setRoot(getTreeNode());
            if(tree != null){
                tree.doLayout();
                System.out.println("do layout");
            }
        }
    }

    public void clearData() {
        if(h3Model!= null){
            h3Model.clear();
        }
        if(codeDetailView!= null){
            codeDetailView.clearEditor();
        }
    }


    private JComponent getSecondPanel(){

        JPanel panel = new JPanel(new BorderLayout());
        //输入框
//        edit = new EditorTextField();
//        edit.setOneLineMode(true);
//        edit.setSize(panel.getWidth(),30);
//        panel.add(edit,BorderLayout.NORTH);

        JBSplitter splitter = new JBSplitter(false,0.5f);
        splitter.setFirstComponent(getH3List());
        splitter.setSecondComponent(getBookCode());
        panel.add(splitter,BorderLayout.CENTER);
        return panel;
    }


    private JComponent getBookCode(){
        codeDetailView = new CodeDetailView(project);
        return codeDetailView;
    }


    private JComponent getH3List(){
        List<TagH3> list3 = new ArrayList<>();
        h3Model = new DefaultListModel<TagH3>();
        h3List = new JBList<>(h3Model);
        h3List.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        h3List.setCellRenderer(new StringRender<TagH3>());
        h3List.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting()){
                    TagH3 v =  h3List.getSelectedValue();
                    System.out.println("current value "+v.toString());
                }

            }
        });
        h3List.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    DefaultActionGroup actions = new DefaultActionGroup();
                    actions.add(new AnAction("change name","change name", AllIcons.Actions.Edit) {
                        @Override
                        public void actionPerformed(@NotNull AnActionEvent e) {
                            String tagName = PopUtil.getInputText("change tag name","please change tag name ");
                            if(TextUtils.isEmpty(tagName)){
                                return;
                            }

                            String newTagName = tagName.trim();

                            TagH3 th3 = h3List.getSelectedValue();
                            int index = h3List.getSelectedIndex();
                            service.getStore().reNameTagH3(selectTag,selectTagChild,th3.name,newTagName);
                            th3 = h3Model.get(index);
                            th3.name = newTagName;
                            h3Model.notify();

                        }
                    });


                    actions.add(new AnAction("remove","remove", AllIcons.General.Remove) {
                        @Override
                        public void actionPerformed(@NotNull AnActionEvent e) {
                            TagH3 th3 = h3List.getSelectedValue();
                            service.getStore().removeTagH3(selectTag,selectTagChild,th3.name);
                            h3Model.removeElement(th3);
                        }
                    });

                    JBPopupFactory.getInstance().createActionGroupPopup(
                            null,
                            actions,
                            DataManager.getInstance().getDataContext(tree),
                            false,
                            null,
                            10
                    ).show(new RelativePoint(e));
                } else if(SwingUtilities.isLeftMouseButton(e)){
                    TagH3 tagH3 = h3List.getSelectedValue();
                    if (tagH3 != null) {
                        VirtualFile file = BUtils.getFileByUrl(tagH3.getFileUrl());
                        VirtualFile projectBase = LocalFileSystem.getInstance().findFileByPath(project.getBasePath());
                        boolean inProject = true;
                        if(file == null){
                            inProject = false;
                        } else{
                            inProject = VfsUtilCore.isAncestor(projectBase, file, true);
                        }
                        if(inProject){
                            codeDetailView.navigateInPreviewEditor(CodeDetailView.PreviewEditorState.create(file, tagH3.getLineNumber()));
                        } else {
                            VirtualFile thisFile = BUtils.getFileByRelativePath(project,tagH3.getRelativePath());
                            System.out.println(" ======  create new vFile2    "+thisFile.getPath());
                            codeDetailView.navigateInPreviewEditor(CodeDetailView.PreviewEditorState.create(thisFile, tagH3.getLineNumber()));
                        }

                    } else {
                        codeDetailView.clearEditor();
                    }
                }
                System.out.println("BookMarkPanel getClickCount "+e.getClickCount());
                if( e.getClickCount() == 2){
                    TagH3 tagH3 = h3List.getSelectedValue();
                    if (tagH3 != null) {
                        System.out.println(" ======  in project   tagH3  "+tagH3);
                        VirtualFile file = BUtils.getFileByUrl(tagH3.getFileUrl());
                        VirtualFile projectBase = LocalFileSystem.getInstance().findFileByPath(project.getBasePath());
                        boolean inProject = true;
                        if(file == null){
                            inProject = false;
                        } else{
                            inProject = VfsUtilCore.isAncestor(projectBase, file, true);
                        }

                        System.out.println(" ====== tagH3   in project    "+inProject);
                        if(inProject){
                            BUtils.jumpToSourceCode(project,file,tagH3.getLineNumber());
                        } else {
                            VirtualFile thisFile = BUtils.getFileByRelativePath(project,tagH3.getRelativePath());
                            System.out.println(" ======  create new vFile    "+thisFile.getPath());
                            BUtils.jumpToSourceCode(project,thisFile,tagH3.getLineNumber());
                        }

                    }
                }

            }
        });
        return h3List;
    }

    private DefaultMutableTreeNode getTreeNode(){
        List<TagH1> list = service.getStore().getTagH1();
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
        System.out.println("getTreeNode  has child "+root.getChildCount());
        return root;
    }
    private JComponent getTreeList() {
        // 创建数据模型
        if (treeModel == null) {
            treeModel = new DefaultTreeModel(getTreeNode());
        }
        if (tree == null) {
            tree = new Tree(treeModel);
            tree.setExpandableItemsEnabled(true);
            tree.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if(tree.getSelectionPath() == null) return;
                    handleSelectPath();
                    String path = tree.getSelectionPath().toString();
                    if (!TextUtils.isEmpty(path)) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (TextUtils.isEmpty(selectTag)) return;
                            DefaultActionGroup actions = new DefaultActionGroup();
                            if (TextUtils.isEmpty(selectTagChild)) {
                                actions.add(new AnAction("add sub tag","add", AllIcons.General.Add) {
                                    @Override
                                    public void actionPerformed(@NotNull AnActionEvent e) {
                                        if (TextUtils.isEmpty(selectTag)) {
                                            return;
                                        }
                                        TagH1 tagH1 = service.getStore().getTagH1ByName(selectTag);
                                        if (tagH1 == null) {
                                            return;
                                        }
                                        String text = PopUtil.getInputText("create new sub folder ", "please enter new Name");
                                        System.out.println(" h1 tag " + tagH1.name + " input tag  " + text);
                                        if (!TextUtils.isEmpty(text)) {
                                            if (TagUtil.hasSameTagH2(tagH1, text)) {
                                                Messages.showErrorDialog("'" + text + "'" + " has been used ", "tag name error");
                                                return;
                                            }
                                            TagH2 h2 = new TagH2(text);
                                            service.getStore().addTagH2(tagH1, h2);
                                            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                                            if (selectedNode == null) return;
                                            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(text);
                                            treeModel.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());

                                            TreeNode[] nodes = treeModel.getPathToRoot(newNode);
                                            TreePath path = new TreePath(nodes);
                                            tree.scrollPathToVisible(path);
                                        }

                                    }
                                });
                            }
                            actions.add(new AnAction("change name","change name", AllIcons.Actions.Edit) {
                                @Override
                                public void actionPerformed(@NotNull AnActionEvent e) {

                                    String tagName = PopUtil.getInputText("change tag name","please change tag name ");
                                    if(TextUtils.isEmpty(tagName)){
                                        return;
                                    }
                                    String newTagName = tagName.trim();

                                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                                    if (TextUtils.isEmpty(selectTagChild)) {
                                        service.getStore().reNameTagH1(selectTag.trim(),newTagName);
                                        selectedNode.setUserObject(newTagName);
                                        if (selectedNode != null && selectedNode.getParent() != null) treeModel.nodeChanged(selectedNode);
                                    } else {
                                        service.getStore().reNameTagH2(selectTag, selectTagChild,newTagName);
                                        selectedNode.setUserObject(newTagName);
                                        if (selectedNode != null && selectedNode.getParent() != null) treeModel.nodeChanged(selectedNode);
                                    }
                                }
                            });

                            actions.add(new AnAction("remove","remove", AllIcons.General.Remove) {
                                @Override
                                public void actionPerformed(@NotNull AnActionEvent e) {
                                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                                    if (TextUtils.isEmpty(selectTagChild)) {
                                        service.getStore().removeTagH1(selectTag.trim());
                                        if (selectedNode != null && selectedNode.getParent() != null) treeModel.removeNodeFromParent(selectedNode);
                                    } else {
                                        service.getStore().removeTagH2(selectTag, selectTagChild);
                                        if (selectedNode != null && selectedNode.getParent() != null) treeModel.removeNodeFromParent(selectedNode);
                                    }
                                }
                            });
                            JBPopupFactory.getInstance().createActionGroupPopup(
                                    null,
                                    actions,
                                    DataManager.getInstance().getDataContext(tree),
                                    false,
                                    null,
                                    10
                            ).show(new RelativePoint(e));
                        }
                    }
                }
            });
        }

        return tree;
    }

    private void handleSelectPath(){
        String path = tree.getSelectionPath().toString();
        System.out.println("handleSelectPath  ");
        if(!TextUtils.isEmpty(path)){
            selectTag = "";
            selectTagChild = "";
            codeDetailView.clearEditor();
            path = path.replace("[", "");
            path = path.replace("]", "");
            String[] arr = path.split(",");
            if (arr.length >= 2) {
                selectTag = arr[1];
            }
            if (arr.length == 3) {
                selectTagChild = arr[2];
            }
            if (h3Model != null) {
                h3Model.clear();
                if (!TextUtils.isEmpty(selectTagChild)) {
                    List<TagH3> l3 = service.getStore().getTagH3(selectTag, selectTagChild);
//                                System.out.println("current list size " + l3.size());
                    Iterator<TagH3> it3 = l3.iterator();
                    while (it3.hasNext()){
                        TagH3 tagH3 = it3.next();
                        h3Model.addElement(tagH3);
                    }

                    h3List.updateUI();
                }
            }
        }
    }

    private JComponent getBookMarkMenu(){
        DefaultActionGroup actions = new DefaultActionGroup();
        actions.add(new AddAction());
//        actions.add(new EditAction());
//        actions.add(new RemoveAction());
        actions.addSeparator();
        actions.add(new ImportAction());
        actions.add(new ExportAction());
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(App.name, actions, true);
        actionToolbar.setReservePlaceAutoPopupIcon(false);
        actionToolbar.setMinimumButtonSize(new Dimension(20, 20));
        JComponent toolBar = actionToolbar.getComponent();
        toolBar.setBorder(JBUI.Borders.merge(toolBar.getBorder(), JBUI.Borders.emptyLeft(12), true));
        toolBar.setOpaque(false);
        return toolBar;
    }



    private static class CodeDetailView extends DetailViewImpl
    {
        CodeDetailView(Project project) {
            super(project);
        }

        @Override
        protected Editor createEditor(@Nullable Project project, Document document, VirtualFile file) {
            Editor editor = super.createEditor(project, document, file);
            editor.setBorder(JBUI.Borders.empty());
            return editor;
        }
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
            if(!TextUtils.isEmpty(value.fileName)){
                append(" -> ("+value.fileName+"#"+value.lineNumber+")");
            }
            setForeground(UIUtil.getListSelectionForeground(isSelected));
            setBackground(UIUtil.getListSelectionBackground(isSelected));
            return this;
        }
    }
}
