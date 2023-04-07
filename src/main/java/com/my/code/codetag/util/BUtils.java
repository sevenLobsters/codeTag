package com.my.code.codetag.util;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

import java.io.File;

public class BUtils {
    /**
     *
     * @param project
     * @return
     * kt_mould
     */
    private static String getProjectName(Project project){
        if(project != null){
            return project.getName();
        }
        return "";
    }

    /**
     * @param project
     * @return
     * E:/23Studio/kt_mould
     */
    private static String getBasePath(Project project){
        if(project != null){
            return project.getBasePath();
        }
        return "";
    }

    /**
     * @param file
     * @return
     * E:/23Studio/kt_mould/src/main/kotlin/skil/ChainOfResponse.kt
     */
    private static String getAbsFilePath(VirtualFile file){
        String result = "";
        if(file != null){
            result = file.getCanonicalPath();
        }
        return result;
    }


    /**
     * 获得文件相对路径。
     * @param project
     * @param file
     * @return
     * src/main/kotlin/skil/ChainOfResponse.kt
     */
    public  static String getRelativeFilePath(Project project,VirtualFile file){
        String s1 = getAbsFilePath(file);
        String s2 = getBasePath(project);
        if(s1.contains(s2)){
            return s1.replace(s2+ File.separator,"");
        }
        return "";
    }


    /**
     * 跳转到指定位置。
     * @param project
     * @param file
     * @param line
     */
    public static void jumpToSourceCode(Project project,VirtualFile file,int line){
        OpenFileDescriptor ofd = new OpenFileDescriptor(project,file,line,-1,true);
        if(ofd.canNavigate()){
            ofd.navigate(true);
        }
    }

    /**
     * 根据uri获得文件
     * @param url
     * @return
     */
    public static VirtualFile getFileByUrl(String url){
        return VirtualFileManager.getInstance().findFileByUrl(url);
    }

    /**
     * 根据相对路径获得文件。
     * @param project
     * @param relativePath
     * @return
     */
    public static VirtualFile getFileByRelativePath(Project project,String relativePath){
        VirtualFile projectBase = LocalFileSystem.getInstance().findFileByPath(project.getBasePath());
        return LocalFileSystem.getInstance().findFileByPath(projectBase.getPath() + File.separator + relativePath);

    }

    /**
     *  是否存在项目中
     * @param project
     * @param file
     * @return
     */
    public static boolean inProject(Project project,VirtualFile file){
        return VfsUtilCore.isAncestor(LocalFileSystem.getInstance().findFileByPath(project.getBasePath()), file, true);
    }



}
