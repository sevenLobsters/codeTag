package com.my.code.codetag;

import com.my.code.codetag.bean.TagH1;
import com.my.code.codetag.bean.TagH2;
import com.my.code.codetag.bean.TagH3;
import com.my.code.codetag.bean.TagH4;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static final String name = "CodeTag";

    public static List<TagH1> getList1(){
        List<TagH1> list = new ArrayList<>();
        TagH1 a = new TagH1("lv01","11");
        TagH1 a2 = new TagH1("lv02","22");
        TagH1 a3 = new TagH1("lv03","333");
        list.add(a);
        list.add(a2);
        list.add(a3);
        return list;
    }
    public static List<TagH1> H1list = new ArrayList<>();


    public static List<TagH1> getRandomH1List(){
        if(H1list.size() != 0){
            return H1list;
        }
        List<TagH1> list  = getList1();
        int i = 1000;
        for(TagH1 b1:list){
            List<TagH2> list2 = new ArrayList<>();
            for(int j = 0;j < 4;j++) {
                String text = b1.name+"_"+i;
                TagH2 bmh2 = new TagH2(text,""+i);
                List<TagH3> l3 = new ArrayList<>();
                for(int k = 0;k <4;k++){
                    String txt = text+"_"+k;
                    TagH3 h3 = new TagH3(""+txt,"");
                    l3.add(h3);
                }
                bmh2.list  = l3;
                list2.add(bmh2);
            }
            b1.list = list2;
        }
        return list;
    }


//    public static List<TagH2> getRandomH2List(){
//        List<TagH1> list1 = getRandomH1List();
//        return list1.get(0).list;
//    }
//
//
//    public static List<TagH3> getRandomH3List(){
//        List<TagH1> list1 = getRandomH1List();
//        return list1.get(0).list.get(0).list;
//    }
//
//    public static List<TagH4> getCode(){
//        List<TagH4> h4 = new ArrayList<>();
//        for(int i =0;i< 3;i++){
//            TagH4 h = new TagH4("this is code  "+i,"");
//            h4.add(h);
//        }
//        return h4;
//    }

}
