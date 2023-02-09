package org.lee.study.refleact;


import java.lang.reflect.Constructor;

/**
 * 构造器对象使用 ，
 */
public class ConstructorDemo {
    public static void main(String[] args) throws Throwable {
        Constructor<?>[] declaredConstructors = PersonDemo.class.getDeclaredConstructors();
        Constructor<?> declaredConstructor = declaredConstructors[0];
        Object o = declaredConstructor.newInstance((Object[]) null);
        if (o instanceof PersonDemo){
            System.out.println(((PersonDemo) o).getName());
        }
    }
}
