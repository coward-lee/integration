package org.lee.complex.consistent.hash;


public class Data implements Comparable<Data>{
    private String key;
    private Integer index;

    public Data(String key, Integer index) {
        this.key = key;
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public int getKeyIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public int compareTo(Data o) {
        return key.compareTo(o.getKey());
    }
}
