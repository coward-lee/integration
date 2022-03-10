package org.lee.study;

import net.sf.cglib.proxy.Enhancer;

public class Main {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TargetObj.class);
        enhancer.setCallback(new TargetInterceptor());
        TargetObj targetObj = (TargetObj) enhancer.create();
        System.out.println(targetObj);
        System.out.println(targetObj.method1("xxx"));
        System.out.println(targetObj.method2(111));
        System.out.println(targetObj.method3("xxxxxxxx"));
    }
}
