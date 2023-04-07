package com.my.code.codetag.bean;

import com.intellij.openapi.vfs.VirtualFile;

public class TagH4 extends BookMarkNode {
    public VirtualFile file;
    private int lineNumber = 0;
    private String fileUrl = "";
    private String relativePath = "";
    private boolean inProject = true;

    public TagH4(String name, String des){
        this.name = name;
        this.des = des;
    }
    String code;
    public boolean isValid(){
        return true;
    }


    @Override
    public String toString() {
        return "BMH4{" +
                "file=" + file +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", des='" + des + '\'' +

                '}';
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public boolean isInProject() {
        return inProject;
    }

    public void setInProject(boolean inProject) {
        this.inProject = inProject;
    }
}
