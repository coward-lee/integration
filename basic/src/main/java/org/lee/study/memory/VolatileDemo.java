package org.lee.study.memory;

public class VolatileDemo {
    private volatile static boolean flag = true;
    public static void main(String[] args) throws Throwable {
        new Thread(()->{
            System.out.println("started thread1");
            while (flag){

            }
            System.out.println("end thread1");
        }).start();

        Thread.sleep(1000);
        new Thread(()->{
            System.out.println("started thread2");
            flag = false;
            System.out.println("end thread2");
        }).start();
        Thread.sleep(2000);
    }
}