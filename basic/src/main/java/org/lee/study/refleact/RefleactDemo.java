package org.lee.study.refleact;

public class RefleactDemo {
    public static void main(String[] args) throws Exception {

        // 不会执行static 代码块
        Class<?> aClass1 = ClassLoader.getSystemClassLoader().loadClass(ReDemo.class.getCanonicalName());
        System.out.println(aClass1.getName());
        // 会执行static 代码块
        Class<?> aClass = Class.forName(ReDemo.class.getCanonicalName());
        System.out.println(aClass.getName());
        System.out.println(ReDemo.i);
    }
}

class ReDemo {
    static int i = 200;

    static {
        System.out.println("statuc load" + i);
        System.out.println("static load");
        i = 100;
    }

    void demo() {
        System.out.println("demo");
    }
}
