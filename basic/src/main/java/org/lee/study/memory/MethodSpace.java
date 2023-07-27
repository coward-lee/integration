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

class MethodSpaceDemo {
    static byte[] bytes = new byte[1024 * 1024 * 1024];
    static String str;

    public void doSomething() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 1024 * 1024 * 1024; i++) {
            stringBuilder.append(" ");
        }
        str = stringBuilder.toString().intern();
    }

    /**
     * 操作数栈
     * 每一次方法调用都会产生一个操作数栈，
     *  这个东西还可以在编译器将最大深度确定，如下面的方法
     *      最大升读自然为4咯，这个在编译阶段就可以确定，所以不用动态扩展
     *  如下代码（当代码执行到了int k = i + j;），此时的操作数栈存储了 i,j的值； 所以操作数栈就是存储计算过程中的中间结果
     */
    public void operationItemStack() {
        int i = 1;
        int j = 1;
        int k = i + j;
        int m = i + j + k;
        int l = i + j + k + m;

    }
}
