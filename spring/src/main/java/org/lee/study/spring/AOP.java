package org.lee.study.spring;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect

public class AOP {
    private final  Logger log = LoggerFactory.getLogger(AOP.class);



        @Before("execution(* org.lee.study.spring.*.*(..))")
        public void before(){
            log.info("AOPJDBCIndex");
        }


        @After("execution(* org.lee.study.spring.*.*(..))")
        public void silenceCellPhones() {
           log.info("AOPJDBCIndex after");
        }


        @Around("execution(* org.lee.study.spring.*.*(..))")
        public void around(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("AOPJDBCIndex around");
            Object proceed = joinPoint.proceed();
            log.info("AOPJDBCIndex around");
        }
    }
