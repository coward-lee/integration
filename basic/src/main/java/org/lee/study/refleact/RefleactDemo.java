package org.lee.study.refleact;

import reactor.core.publisher.Flux;

import java.net.URL;
import java.util.List;

public class RefleactDemo {
    public static void main(String[] args) throws Exception {

        // 不会执行static 代码块
        Class<?> aClass1 = ClassLoader.getSystemClassLoader().loadClass(ReDemo.class.getCanonicalName());
        System.out.println(aClass1.getName());


        // 会执行static 代码块

        Class<?> aClass = Class.forName(ReDemo.class.getCanonicalName());
        System.out.println(aClass.getName());
//        System.out.println();
        System.out.println(ReDemo.i);


        // 只会加载父类
        System.out.println(Child.par);
        System.out.println("只加载父类验证");
        // 这个都加载子类和父类
        System.out.println(Class.forName(Child.class.getCanonicalName()));

//        System.out.println(URLClassPath.);
        System.out.println(RefleactDemo.class.getClassLoader().getResource("").getPath());
        Flux.fromIterable(List.of(""));
        System.out.println("finished");
        // 打破双亲委派模型

    }
}

class MyClassLoader extends ClassLoader {
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return getParent().loadClass(name);
    }

    @Override
    protected URL findResource(String name) {
        return super.findResource(name);
    }
}

class ReDemo {

    public ReDemo() {
        System.out.println("initialized");
    }

    static int i = 200;

    static {
        // static 是顺序执行的如果 变量i的定义在 引用之后会访问错误
        i = 100;
        System.out.println("statuc load" + i);
        System.out.println("static load");
    }
//    static int i = 200;

    void demo() {
        System.out.println("demo");
    }
}

class Parent {
    public static int par = 0;

    static {
        System.out.println("parent");
    }
}

class Child extends Parent {
    static int child = 10;

    static int newPar = par;

    static {
        System.out.println("child ");
    }

    public Child() {
        super();
    }
}