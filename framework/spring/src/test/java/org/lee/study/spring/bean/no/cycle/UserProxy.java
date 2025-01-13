package org.lee.study.spring.bean.no.cycle;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

// 增强的类
@Component
@Aspect
public class UserProxy {
    public UserProxy() {
        System.out.println("user proxy");
    }

    // 相同的切入掉
    @Pointcut("execution(*  org.lee.study.spring.bean.no.cycle.*.*(..))")
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
