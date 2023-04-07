package com.my.code.codetag.util;

import com.intellij.openapi.project.Project;
import com.my.code.codetag.CodeTagService;
import com.my.code.codetag.bean.TagH1;
import com.my.code.codetag.bean.TagH2;
import com.my.code.codetag.bean.TagH3;
import com.my.code.codetag.bean.TagH4;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.util.TextUtils;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TagUtil {


    //<bookMark>
    //    <h1>
    //        <name>页面</name>
    //        <des></des>
    //        <h2>
    //            <name>启动页</name>
    //            <h3>
    //                <name>接口</name>
    //                <des>描述</des>
    //                    <lineNumber>94</lineNumber>
    //                    <inProject>true</inProject>
    //                    <fileUrl>
    //                        file://D:/ANewSpace/2022/gspace/HHDDZ/proj.android/main_jar/src/main/java/com/excelliance/kxqp/gs/launch/function/CheckAPkCpu.java
    //                    </fileUrl>
    //                    <relativePath>main_jar/src/main/java/com/excelliance/kxqp/gs/launch/function/CheckAPkCpu.java
    //            </h3>
    //        </h2>
    //    </h1>
    //
    //    <h1>
    //    </h1>
    //    <h1>
    //    </h1>
    //</bookMark>
    public static List<TagH1> readData(Element element){
        List<TagH1> list = new ArrayList<>();
//        System.out.println("readData  text  "+element.getText()+" name "+element.getName());
//        for(Element e : element.getChildren()){
//            System.out.println("readData  text  "+e.getText()+" name "+e.getName());
//        }

//        Element root = element.getChild("CodeTag");
//        System.out.println("readData  size  "+element.getChildren().size());
        try{
             for(Element h1:element.getChildren("h1")){
                 String name = h1.getChild("name").getText();
                 String des = h1.getChild("des").getText();
//                 System.out.println("readData h1  name  "+name+"  des "+des);
                 TagH1 t = new TagH1(name,des);
                 List<TagH2> l2 = new ArrayList<>();
                 for(Element h2: h1.getChildren("h2")){
                     String n2 = h2.getChild("name").getText();
                     String d2 = h2.getChild("des").getText();
                     TagH2 t2 = new TagH2(n2,d2);
//                     System.out.println("TagUtil readData h2  name  "+n2+"  des "+d2);
                     List<TagH3> l3 = new ArrayList<>();
                     for(Element h3:h2.getChildren("h3")){
                         String n3 = h3.getChild("name").getText();
                         String des3 = h3.getChild("des").getText();
                         String fileName = h3.getChild("fileName").getText();
                         String lineNumber = h3.getChild("lineNumber").getText();
                         String fileUrl = h3.getChild("fileUrl").getText();
                         String relativePath = h3.getChild("relativePath").getText();
                         String inProject = h3.getChild("inProject").getText();
                         boolean inTheProject = false;
                         if(!TextUtils.isEmpty(inProject) && "true".equals(inTheProject)){
                             inTheProject = true;
                         }
                         int ln = 0;
                         try{
                             ln = Integer.valueOf(lineNumber);
                         } catch (Exception e){
                             e.printStackTrace();
                         }

                         TagH3 t3 = new TagH3(n3,fileUrl,relativePath,ln,inTheProject,fileName);
//                         List<TagH4> l4 = new ArrayList<>();
//                         for(Element h4:h3.getChildren("h4")){
//                             String n4 = h4.getChild("name").getText();
//                             String d4 = h4.getChild("des").getText();
//                             TagH4 t4 = new TagH4(n4,d4);
//                             l4.add(t4);
//                         }
//                         t3.list =l4;
//                         System.out.println("TagUtil readData  t3 "+t3);
                         l3.add(t3);
                     }
                     t2.list = l3;
                     l2.add(t2);
                 }
                 t.list = l2;
                 list.add(t);
             }
        } catch (Exception e){
            e.printStackTrace();
        }
//        System.out.println("readData number is   "+list.size());
        return list;
    }

    public static Element buildElement(Iterator<TagH1> iterator){
        Element root = new Element("tag");
//        System.out.println("root  hasNext "+iterator.hasNext());
        while (iterator.hasNext()) {
            TagH1 h1 = iterator.next();
            Element elementH1 = new Element("h1");
            elementH1.addContent(new Element("name").addContent(h1.name));
//            System.out.println("TagUtil buildElement h1  name "+h1);
            elementH1.addContent(new Element("des").addContent(h1.des));
//            System.out.println("h1  des "+h1.des);
            Iterator<TagH2> it2 = h1.list.iterator();
            while (it2.hasNext()){
                TagH2 h2 = it2.next();
                Element eh2 = new Element("h2");
                eh2.addContent(new Element("name").addContent(h2.name));
                eh2.addContent(new Element("des").addContent(h2.des));
//                System.out.println("TagUtil buildElement h2  name "+h2+" child.size "+h2.list.size());
                Iterator<TagH3> it3 = h2.list.iterator();
                while (it3.hasNext()){
                    TagH3 h3 = it3.next();
//                    System.out.println("TagUtil buildElement  h3 "+h3);
                    Element eh3= new Element("h3");
                    eh3.addContent(new Element("name").addContent(h3.name));
                    eh3.addContent(new Element("des").addContent(h3.des));
                    eh3.addContent(new Element("fileName").addContent(h3.fileName));
                    eh3.addContent(new Element("lineNumber").addContent(h3.getLineNumber()+""));
                    eh3.addContent(new Element("fileUrl").addContent(h3.getFileUrl()));
                    eh3.addContent(new Element("relativePath").addContent(h3.getRelativePath()));
                    eh3.addContent(new Element("inProject").addContent(h3.isInProject()?"true":"false"));
//                    Iterator<TagH4> it4 = h3.list.iterator();
//                    while (it4.hasNext()){
//                        TagH4 h4 = it4.next();
//                        Element eh4= new Element("h4");
//                        eh4.addContent(new Element("name").addContent(h4.name));
//                        eh4.addContent(new Element("des").addContent(h4.des));
//                        eh3.addContent(eh4);
//                    }
                    eh2.addContent(eh3);
                }
                elementH1.addContent(eh2);
            }

            root.addContent(elementH1);
        }
//        System.out.println("buildElement root size "+root.getChildren().size()+"  name "+root.getName()+"  value "+root.getText());
        return root;
    }
    public static boolean hadSameTagH1(Project project,String tag){
        if(TextUtils.isEmpty(tag)){
            return false;
        }
        Iterator<TagH1> it = CodeTagService.getInstance(project).getStore().getTagH1().iterator();
        while (it.hasNext()){
            TagH1 h = it.next();
            if(h.name.equals(tag)){
                return true;
            }
        }
        return false;
    }
    public static boolean hasSameTagH2(TagH1 tagH1,String name){
        if(tagH1 == null && CollectionUtils.isEmpty(tagH1.list)){
            return false;
        }
        Iterator<TagH2> it = tagH1.list.iterator();
        while (it.hasNext()){
            TagH2 tagH2 = it.next();
            if(tagH2.name.equals(name)){
                return true;
            }
        }
        return false;
    }

    public static boolean hasSameTagH3(TagH2 tagH2,String name){
        if(tagH2 == null && CollectionUtils.isEmpty(tagH2.list)){
            return false;
        }
        Iterator<TagH3> it = tagH2.list.iterator();
        while (it.hasNext()){
            TagH3 tagH3 = it.next();
            if(tagH3.name.equals(name)){
                return true;
            }
        }
        return false;
    }


    public static void saveToXml(){

    }

    public static void readFromXml(){

    }

}
