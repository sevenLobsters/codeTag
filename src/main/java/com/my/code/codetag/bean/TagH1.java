package com.my.code.codetag.bean;

import java.util.ArrayList;
import java.util.List;

public class TagH1 extends BookMarkNode {

    public TagH1(){

    }

    public TagH1(String name ){
        this.name = name;
    }

    public TagH1(String name, String des){
        this.name = name;
        this.des = des;
    }

    public List<TagH2> list = new ArrayList<>();


}
