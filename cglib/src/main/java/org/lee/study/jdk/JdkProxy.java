package org.lee.study.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JdkProxy implements InvocationHandler {
    private Object realObj;

    public JdkProxy(Object realObj) {
        this.realObj = realObj;
    }


    public JdkProxy() {
        System.out.println("“inti");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = method.invoke(realObj, args);
        return invoke;
    }
}
