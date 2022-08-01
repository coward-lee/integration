package org.lee.study.memory;

public class DemoPojo {

    public byte[] bytes;
    public DemoPojo1 demoPojo1;

    public DemoPojo(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setDemoPojo1(DemoPojo1 demoPojo1) {
        this.demoPojo1 = demoPojo1;
    }
}
