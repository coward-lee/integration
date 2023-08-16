package org.lee.study.memory;

public class VolatileDemo3 {
    volatile  int var = 0;

    public void setVar(int var) {
        System.out.println("setVar="+var);
        this.var = var;
    }

    public static void main(String[] args) {
        VolatileDemo3 demo = new VolatileDemo3();
        demo.setVar(100);
    }
}