package com.my.code.codetag;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.my.code.codetag.bean.TagH1;
import com.my.code.codetag.bean.TagStore;
import com.my.code.codetag.util.TagUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(
        name = App.name,
        storages = {
                @Storage(App.name + ".xml"),
        }
)
public class CodeTagService implements PersistentStateComponent<Element> {
    Project project;
    TagStore store;
//    String lastExportDir = "";
//    String lastImportDir = "";

    public CodeTagService(@NotNull Project project)
    {
        this.project = project;
        store = TagStore.getInstance(project);
    }

    public static CodeTagService getInstance(@NotNull Project project)
    {
        return ServiceManager.getService(project, CodeTagService.class);
    }

    public TagStore getStore(){
        return  store;
    }

    @Override
    public @Nullable Element getState() {
        System.out.println("CodeTagService  getState  size   "+TagStore.getInstance(project).getTagH1().size());
        Element  element = TagUtil.buildElement(TagStore.getInstance(project).getTagH1().iterator());
//        Element state = new Element("state");
//        state.setAttribute("lastExportDir", lastExportDir());
//        state.setAttribute("lastImportDir", lastImportDir());
//        element.addContent(state);
        return element;
    }


    @Override
    public void loadState(@NotNull Element state) {

        try {
            System.out.println("CodeTagService  loadState "+state.toString());
            TagStore.getInstance(project).setRootTag(TagUtil.readData(state));
        } catch (Exception e) {
            TagStore.getInstance(project).setRootTag(new ArrayList<>());
            System.out.println("loadState  "+e.getMessage());
        }
//        Element stateElement = state.getChild("state");
//        lastExportDir = stateElement.getAttributeValue("lastExportDir");
//        lastImportDir = stateElement.getAttributeValue("lastImportDir");
    }

//    public String lastExportDir() { return lastExportDir != null ? lastExportDir : ""; }
//    public void setLastExportDir(String lastExportDir) { this.lastExportDir = lastExportDir; }
//
//    public String lastImportDir() { return lastImportDir != null ? lastImportDir : ""; }
//    public void setLastImportDir(String lastImportDir) { this.lastImportDir = lastImportDir; }
}
