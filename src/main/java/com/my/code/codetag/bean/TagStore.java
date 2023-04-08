package com.my.code.codetag.bean;

import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.Topic;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.codehaus.groovy.util.StringUtil;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TagStore {

    private Project project;
    private static TagStore obj = null;
    public interface H1Change {
        Topic<H1Change> TAG_H1 = Topic.create("H1_MSG_TOPIC", H1Change.class);

        void onAddTagH1(TagH1 tagH1);
        void onRemoveTagH1(TagH1 tagH1);

        void loadTagH1();

    }

    public interface H2Change {
        Topic<H2Change> TAG_H2 = Topic.create("H2_MSG_TOPIC", H2Change.class);

        void onAddTagH2(TagH1 parent,TagH2 tagH);
        void onRemoveTagH2(TagH1 parent,TagH2 tagH);

    }

    public interface H3Change {
        Topic<H3Change> TAG_H3 = Topic.create("H3_MSG_TOPIC", H3Change.class);

        void onAddTagH3(TagH2 parent,TagH3 tagH);

        void onAddTagH3ByName(String t1,String t2,TagH3 h3);
        void onRemoveTagH3(TagH2 parent,TagH3 tagH);

    }


    private static ArrayList<TagH1> rootTag = new ArrayList<>();


    private TagStore(Project project){
        this.project = project;
    }

    public static TagStore getInstance(Project project){
        if(obj == null){
            obj = new TagStore(project);
        }
        return obj;
    }

    public void setRootTag(List<TagH1> list){
        if(CollectionUtils.isEmpty(list)) return;
        rootTag.clear();
        rootTag.addAll(list);
    }

    public List<TagH1> getTagH1(){
        return rootTag;
    }

    public TagH1 getTagH1ByName(String tagName){
        if(TextUtils.isEmpty(tagName)|| CollectionUtils.isEmpty(rootTag)){
            return null;
        }
        Iterator<TagH1> it = rootTag.iterator();
        while (it.hasNext()){
            TagH1 tagH1 = it.next();
            if(tagName.trim().equals(tagH1.name)){
                return tagH1;
            }
        }
        return null;
    }

    public List<TagH3> getTagH3(String tag1,String subTag){
        List<TagH3> list = new ArrayList<>();
        if(CollectionUtils.isEmpty(rootTag)||TextUtils.isEmpty(tag1) || TextUtils.isEmpty(subTag)){
            return list;
        }
        System.out.println("TagStore   tag1="+tag1+"  subTag="+subTag+" root size "+rootTag.size());
        Iterator<TagH1> it1 = rootTag.iterator();
        while (it1.hasNext()){
            TagH1 h1 = it1.next();
            if(h1.name.trim().equals(tag1.trim())){
                List<TagH2> l2 = h1.list;
                Iterator<TagH2> it2 = l2.listIterator();
                while (it2.hasNext()){
                    TagH2 h2 = it2.next();
                    if(subTag.trim().equals(h2.name.trim())){
                        if(!CollectionUtils.isEmpty(h2.list)){
                            list.addAll(h2.list);
                        }
                    }
                }
            }
        }
        return list;
    }



    public void addTagH1(TagH1 h1){
        if(h1== null) return;
        rootTag.add(h1);
        MessageBus messageBus = project.getMessageBus();
        H1Change publisher = messageBus.syncPublisher(H1Change.TAG_H1);
        publisher.onAddTagH1(h1);

    }


    public void addTagH2(TagH1 h1,TagH2 h2){
        if(h2 == null||h1 == null) return;
        if(!CollectionUtils.isEmpty(rootTag)){
            for(TagH1 h : rootTag){
                if(h.name.equals(h1.name)){
                    h.list.add(h2);
//                    System.out.println("TagStore= add h2 "+h2.name);
                    break;
                }
            }
        }
        MessageBus messageBus = project.getMessageBus();
        H2Change publisher = messageBus.syncPublisher(H2Change.TAG_H2);
        publisher.onAddTagH2(h1,h2);
    }

    public void addTagH3(TagH2 h2,TagH3 h3){
        if(h2 == null||h3 == null) return;
        h2.list.add(h3);
        MessageBus messageBus = project.getMessageBus();
        H3Change publisher = messageBus.syncPublisher(H3Change.TAG_H3);
        publisher.onAddTagH3(h2,h3);
    }

    public void addTagH3(String th1,String th2,TagH3 tagH3){
       if(TextUtils.isEmpty(th1)||TextUtils.isEmpty(th2)||tagH3 == null){
           return;
       }
//        System.out.println("TagStore addTagH3 th1  "+th1+"  th2 "+th2+"  tagH3 "+tagH3);
        if(!CollectionUtils.isEmpty(rootTag)){
            for(TagH1 h : rootTag){
//                System.out.println(" h "+h.name+" h1"+th1+"  equals "+h.name.equals(th1)+" other "+ StringUtils.equals(h.name,th1));
                if(h.name.equals(th1)){
                    List<TagH2> list = h.list;
                    Iterator<TagH2> it = list.iterator();
                    while (it.hasNext()){
                        TagH2 tagH2 = it.next();
//                        System.out.println(" tagH2.name "+tagH2.name+" th2"+th2+"  equals "+tagH2.name.equals(th2));
                        if(tagH2.name.equals(th2)){
//                            System.out.println("TagStore addTagH3 data success parent is "+tagH2);
                            tagH2.list.add(tagH3);
                        }
                    }
                    break;
                }
            }
        }
        MessageBus messageBus = project.getMessageBus();
//        System.out.println("TagStore addTagH3 data  "+tagH3);
        TagStore.H3Change publisher = messageBus.syncPublisher(TagStore.H3Change.TAG_H3);
        publisher.onAddTagH3ByName(th1,th2,tagH3);

    }

    public void removeTagH1(String tag){
        if(TextUtils.isEmpty(tag)) return;
        TagH1 del = null;
        Iterator<TagH1> it = rootTag.iterator();
        while (it.hasNext()){
            TagH1 t = it.next();
            if(tag.equals(t.name.trim())){
                del = t;
                System.out.println("remove tagH1  "+tag);
                it.remove();
                break;
            }
        }
        if(del != null){
            MessageBus messageBus = project.getMessageBus();
            H1Change publisher = messageBus.syncPublisher(H1Change.TAG_H1);
            publisher.onRemoveTagH1(del);
        }
    }

    public void reNameTagH1(String oldTag,String newTag){
        if(TextUtils.isEmpty(oldTag)) return;
        TagH1 del = null;
        Iterator<TagH1> it = rootTag.iterator();
        while (it.hasNext()){
            TagH1 t = it.next();
            if(oldTag.equals(t.name.trim())){
                t.name = newTag;
//                System.out.println("reNameTag tagH1  "+newTag);
                break;
            }
        }
    }

    public void removeTagH2(String parentTag,String subTag){
        if(!TextUtils.isEmpty(parentTag) && !TextUtils.isEmpty(subTag)){
            TagH1 findTag = null;
            Iterator<TagH1> it = rootTag.iterator();
            while (it.hasNext()){
                TagH1 t = it.next();
                if(parentTag.trim().equals(t.name.trim())){
                    findTag = t;
                    break;
                }
            }
            if(findTag != null){
                Iterator<TagH2> it2 = findTag.list.iterator();
                while (it2.hasNext()){
                    TagH2 th2 = it2.next();
                    if(th2.name.trim().equals(subTag.trim())){
                        System.out.println("removeTagH2 remove success "+subTag);
                        it2.remove();
                        break;
                    }
                }
            }
        }
    }

    public void reNameTagH2(String parentTag,String oldTagH2,String newTagH2){
        if(!TextUtils.isEmpty(parentTag) && !TextUtils.isEmpty(oldTagH2)){
            TagH1 findTag = null;
            Iterator<TagH1> it = rootTag.iterator();
            while (it.hasNext()){
                TagH1 t = it.next();
                if(parentTag.trim().equals(t.name.trim())){
                    findTag = t;
                    break;
                }
            }
            if(findTag != null){
                Iterator<TagH2> it2 = findTag.list.iterator();
                while (it2.hasNext()){
                    TagH2 th2 = it2.next();
                    if(th2.name.trim().equals(oldTagH2.trim())){
                        th2.name = newTagH2;
                        break;
                    }
                }
            }
        }
    }


    public void removeTagH3(String h1,String h2,String h3){
//        System.out.println("removeTagH3   h1="+h1+",  h2="+h2+" ,h3 ="+h3);
        if(!TextUtils.isEmpty(h1) && !TextUtils.isEmpty(h2) && !TextUtils.isEmpty(h3)){
            TagH1 findTag = null;
            Iterator<TagH1> it = rootTag.iterator();
//            System.out.println("rootTag  size "+rootTag.size());
            while (it.hasNext()){
                TagH1 t = it.next();
                System.out.println("TagH1 root name "+t.name+" h1 "+h1+" h1.equals(t.name)   "+h1.equals(t.name));
                if(h1.trim().equals(t.name.trim())){
//                    System.out.println("h1 find "+t.name);
                    findTag = t;
                    break;
                }
            }
            TagH2 findH2 = null;
            if(findTag != null){
                Iterator<TagH2> it2 = findTag.list.iterator();
                while (it2.hasNext()){
                    TagH2 th2 = it2.next();
                    System.out.println("TagH2 list th2 name "+th2.name+" h2 "+h2+" th2.name.equals(h2) "+th2.name.equals(h2));
                    if(th2.name.trim().equals(h2.trim())){
//                        System.out.println("h2 find "+th2.name);
                        findH2 = th2;
                        break;
                    }
                }
            }

            if(findH2 != null){
                Iterator<TagH3> it3 = findH2.list.iterator();
                while (it3.hasNext()){
                    TagH3 th3 = it3.next();
//                    System.out.println("TagH3 th3 root name "+th3.name+" h1 "+h3);
                    if(th3.name.trim().equals(h3.trim())){
//                        System.out.println("h3 find "+th3.name);
                        it3.remove();
                        break;
                    }
                }
            }
        }
    }


    public void reNameTagH3(String h1,String h2,String oldH3,String newH3){
//        System.out.println("removeTagH3   h1="+h1+",  h2="+h2+" ,h3 ="+oldH3);
        if(!TextUtils.isEmpty(h1) && !TextUtils.isEmpty(h2) && !TextUtils.isEmpty(oldH3) && !TextUtils.isEmpty(newH3)){
            TagH1 findTag = null;
            Iterator<TagH1> it = rootTag.iterator();
//            System.out.println("rootTag  size "+rootTag.size());
            while (it.hasNext()){
                TagH1 t = it.next();
//                System.out.println("TagH1 root name "+t.name+" h1 "+h1);
                if(h1.equals(t.name)){
//                    System.out.println("h1 find "+t.name);
                    findTag = t;
                    break;
                }
            }
            TagH2 findH2 = null;
            if(findTag != null){
                Iterator<TagH2> it2 = findTag.list.iterator();
                while (it2.hasNext()){
                    TagH2 th2 = it2.next();
//                    System.out.println("TagH2 th2 root name "+th2.name+" h1 "+h2);
                    if(th2.name.equals(h2)){
//                        System.out.println("h2 find "+th2.name);
                        findH2 = th2;
                        break;
                    }
                }
            }

            if(findH2 != null){
                Iterator<TagH3> it3 = findH2.list.iterator();
                while (it3.hasNext()){
                    TagH3 th3 = it3.next();
//                    System.out.println("TagH3 th3 root name "+th3.name+" h1 "+oldH3);
                    if(th3.name.equals(oldH3)){
                        th3.name = newH3;
                        break;
                    }
                }
            }
        }
    }


    public void addTagH1byName(String name){
        if(TextUtils.isEmpty(name)) return;
        TagH1 h = new TagH1(name);
        rootTag.add(h);
    }

}
