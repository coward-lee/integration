package org.lee.study.thread;

import java.io.IOException;

public class ThreadLocalDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("执行完成了");
        new Thread(() -> {
            demo();
            try {
                System.in.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println("somthing close");

            }
        }, "user").start();
        Thread.sleep(Integer.MAX_VALUE);
    }

    static void demo() {
        int _1G = 1024 * 1024 * 1024;
        ThreadLocal<byte[]> threadLocal = new ThreadLocal<>();
        threadLocal.set(new byte[_1G]);
        System.out.println("allocate finished");
//        threadLocal.remove(); 需要手动remove
    }
}
