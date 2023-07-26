package org.lee.study.memory;

/**
 * 方法区
 */
public class MethodSpace {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("demo");
        MethodSpaceDemo methodSpace = new MethodSpaceDemo();
        methodSpace.getClass();
        methodSpace.doSomething();
        Thread.sleep(Integer.MAX_VALUE);

    }
}
class MethodSpaceDemo{
    static byte[] bytes = new byte[1024 * 1024 * 1024];
    static String str;
    public void doSomething(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 1024 * 1024 * 1024; i++) {
            stringBuilder.append(" ");
        }
        str = stringBuilder.toString().intern();
    }
}
