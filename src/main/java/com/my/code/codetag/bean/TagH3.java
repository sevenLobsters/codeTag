package com.my.code.codetag.bean;

import java.util.ArrayList;
import java.util.List;

public class TagH3 extends BookMarkNode {
//    String lineNumber = h3.getChild("lineNumber").getText();
//    String fileUrl = h3.getChild("fileUrl").getText();
//    String relativePath = h3.getChild("relativePath").getText();
//    String inProject = h3.getChild("inProject").getText();


    private String fileUrl = "";
    private String relativePath = "";
    private boolean inProject = false;

    public TagH3(){

    }
    public TagH3(String name){
        this.name = name;
    }

    public TagH3(String name,String fileUrl,String relativePath,int lineNumber,boolean inProject){
        this.name = name;
        this.fileUrl = fileUrl;
        this.relativePath = relativePath;
        this.lineNumber = lineNumber;
        this.inProject = inProject;
    }
    public TagH3(String name,String fileUrl,String relativePath,int lineNumber,boolean inProject,String fileName){
        this.name = name;
        this.fileUrl = fileUrl;
        this.relativePath = relativePath;
        this.lineNumber = lineNumber;
        this.inProject = inProject;
        this.fileName = fileName;
    }
    public TagH3(String name, String des){
        this.name = name;
        this.des = des;
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

    @Override
    public String toString() {
        return "TagH3{" +
                "lineNumber=" + lineNumber +
                ", fileUrl='" + fileUrl + '\'' +
                ", relativePath='" + relativePath + '\'' +
                ", inProject=" + inProject +
                ", name='" + name + '\'' +
                '}';
    }
}
