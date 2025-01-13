package org.lee.complex.consistent.hash;

import java.util.Set;
import java.util.TreeSet;

public class Node {

    private String name;
    // 这个是在hash 环上的位置
    private Integer index;
    private Set<Data> data;

    public Node(Integer index,String name) {
        this.name = name;
        this.data = new TreeSet<>();
        this.index = index;
    }

    public Set<Data> getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return index;
    }
    public void addData(Data datum){
        data.add(datum);
    }
}
