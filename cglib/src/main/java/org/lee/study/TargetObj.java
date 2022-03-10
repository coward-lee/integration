package org.lee.study;

public class TargetObj {

    public TargetObj() {
        System.out.println("默认构造器");
    }
    public TargetObj(String name) {

        System.out.println("一个参数的构造器："+name);
    }


    public String method1(String m){
        System.out.println("传入了："+m);
        return "method1";
    }
    public Integer method2(int a){
        return a;
    }
    public int method3(String m){
        System.out.println(m);
        return 3;
    }

    @Override
    public String toString() {
        return "TargetObj []" +getClass();
    }
}
