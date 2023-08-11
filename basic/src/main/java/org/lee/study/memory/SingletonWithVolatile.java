package org.lee.study.memory;

import java.util.DoubleSummaryStatistics;

public class SingletonWithVolatile {

    private static SingletonWithVolatile ins = null;

    public static void main(String[] args) {
        SingletonWithVolatile ins1 = SingletonWithVolatile.getIns();
        System.out.println(ins1);
    }

    private static SingletonWithVolatile getIns(){
        if (ins == null){
            synchronized (DoubleSummaryStatistics.class){
                if (ins == null){
                    ins = new SingletonWithVolatile();
                }
            }
        }
        return ins;
    }
}
