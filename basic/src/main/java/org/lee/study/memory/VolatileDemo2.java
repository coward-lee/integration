package org.lee.study.memory;

import java.util.HashSet;

public class VolatileDemo2 {
    static int a, b, c, d = 0;

    public static void main(String[] args) throws Throwable {

        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < 1000 * 1000 ; i++) {
            a = b = c = d = 0;
            Thread t1 = new Thread(() -> {
                a = c;
                d = 1;
            });
            Thread t2 = new Thread(() -> {
                a = d;
                c = 1;
            });
            t2.start();
            t1.start();
            t2.join();
            t1.join();
            set.add("a=" + a + "," + "b=" + b);
            System.out.println(set);
        }
    }
}