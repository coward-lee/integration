package org.lee.study.util;

public class ThreadUtil {

    public static void sleep(long mill){
        try {
            Thread.sleep(mill);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
