//package org.lee.study.spring.aop.dynamic.proxy.aspectj;
//
//import org.aspectj.lang.annotation.*;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//// 增强的类
//@Component
//@Aspect
//@Order(1)
//public class PersonProxy {
//    // 相同的切入掉
//    @Pointcut("execution(* org.lee.study.spring.aop.dynamic.proxy.aspectj.User.add(..))")
//    public void pointDemo(){
//
//    }
//
//
//    @Before("pointDemo()")
//    public void before(){
//        System.out.println("person before......");
//    }
//
//
//}
