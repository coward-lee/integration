package org.lee.study.spring.aop.dynamic.proxy.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// 增强的类
@Component
@Aspect
@Order(3)
public class UserProxy {
    // 相同的切入掉
    @Pointcut("execution(* org.lee.study.spring.aop.dynamic.proxy.aspectj.User.add(..))")
    public void pointDemo(){

    }


    @Before("pointDemo()")
    public void before(){
        System.out.println("before......");
    }

    @After("pointDemo()")
    public void after(){
        System.out.println("After......");
    }

    @AfterReturning("pointDemo()")
    public void afterReturn(){
        System.out.println("AfterReturning......");
    }

    @AfterThrowing("pointDemo()")
    public void afterThrowing(){
        System.out.println("AfterThrowing......");
    }

    @Around("pointDemo()")
    public void around(ProceedingJoinPoint point)  {
        try{
            System.out.println("Around...... before");
            Object proceed = point.proceed();
            System.out.println("Around...... after");

        }catch (Throwable throwable){
            System.out.println(throwable);
        }
    }
}
