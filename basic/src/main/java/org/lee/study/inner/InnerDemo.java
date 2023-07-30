package org.lee.study.inner;

public class InnerDemo {

    public static void main(String[] args) {
        new InnerDemo().method();
//        new InnerDemo().new Inner();
    }

    void method(){
        final Demo a = new Demo();
        // 内部不能直接修改a的引用
        class Inner{
            void innerMethod(){
                System.out.println(a);
//                a = new Demo();
            }
        }
        new Inner().innerMethod();
        System.out.println(a+"xxxxxxxxxxxxx:"+a.hashCode());
    }

}

class Demo{

}