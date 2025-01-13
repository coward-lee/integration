package org.lee.study.spring.aop.dynamic.proxy.aspectj.annotation;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
public class StopWatchAop {

    @Pointcut("@annotation(org.lee.study.spring.aop.dynamic.proxy.aspectj.annotation.StopWatch)")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object proceed;
        String methodName = getMethodName(point);
        Class<?> clazz = point.getTarget().getClass();
        String args = Arrays.toString(point.getArgs()).replace(']', ')').replace('[', '(');
        StopWatch stopWatch  = new StopWatch(String.format("%s.%s%s", clazz.getName(), methodName, args));
        stopWatch.start();
        proceed = point.proceed();
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        return proceed;
    }

    private String getMethodName(JoinPoint jp) {
        return jp.getSignature().getName();
    }
}
