package com.my.code.codetag.bean;

import org.jetbrains.annotations.NotNull;

public class BookMarkNode implements Comparable<BookMarkNode>{
    public String name = "";
    public String fileName = "";
    public int lineNumber = 0;
    public String des = "";


    @Override
    public int compareTo(@NotNull BookMarkNode o) {
        return name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "BookMarkNode{" +
                "name='" + name + '\'' +
                '}';
    }
}
