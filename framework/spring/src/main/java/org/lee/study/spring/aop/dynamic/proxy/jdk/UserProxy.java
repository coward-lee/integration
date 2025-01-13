package org.lee.study.spring.aop.dynamic.proxy.jdk;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *
 */
// 创建代理对象代码
@Component
public class UserProxy implements InvocationHandler {
    // 1. 创建的是谁的对象，把传进来
    private Object obj;

    public UserProxy(Object obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 方法之前
        System.out.println("方法执行之前。。。"+method.getName()+"传递的参数："+ Arrays.toString(args));
        Object ret = method.invoke(obj, args);
        // 方法之后
        System.out.println("方法执行之后。。。"+obj);
        return ret;
    }
}
