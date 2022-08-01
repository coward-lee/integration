package org.lee.study.memory;

public class DemoPojo1 {
    public byte[] bytes;
    public DemoPojo demoPojo;

    public DemoPojo1(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setDemoPojo(DemoPojo demoPojo) {
        this.demoPojo = demoPojo;
    }
}
