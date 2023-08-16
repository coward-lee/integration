package org.lee.study.memory;

import java.util.HashSet;

public class VolatileDemo2 {
    static int a, b, c, d = 0;
    volatile static int x = 0,y = 0;
    static  int z, w = 0;

    public static void main(String[] args) throws Throwable {
        method2();

    }

    /**
     * 这个会出现 a = 0 ，b = 0的情况，这样就是因为指令重排序的结果
     * @throws InterruptedException
     */
    static void method1() throws InterruptedException {
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < 1000 * 1000; i++) {
            a = b = c = d = 0;
            Thread t1 = new Thread(() -> {
                d = 1;
                a = c;
            });
            Thread t2 = new Thread(() -> {
                c = 1;
                b = d;
            });
            t2.start();
            t1.start();
            t2.join();
            t1.join();
            set.add("a=" + a + "," + "b=" + b);
            System.out.println(set);
        }
    }

    static void method2() throws InterruptedException {
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < 1000 * 1000; i++) {
            x = y = z = w = 0;
            Thread t1 = new Thread(() -> {
                x = 1;
                z = y;
            });
            Thread t2 = new Thread(() -> {
                y = 1;
                w = x;
            });
            t2.start();
            t1.start();
            t2.join();
            t1.join();
            set.add("z=" + z + "," + "w=" + w);
            System.out.println(set);
        }
    }
}