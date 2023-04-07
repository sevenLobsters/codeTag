package com.my.code.codetag.bean;

import java.util.ArrayList;
import java.util.List;

public class TagH2 extends BookMarkNode {


    public TagH2(String name){
        this.name = name;
    }
    public TagH2(String name, String des){
        this.name = name;
        this.des = des;
    }
    public List<TagH3> list = new ArrayList<>();
}
