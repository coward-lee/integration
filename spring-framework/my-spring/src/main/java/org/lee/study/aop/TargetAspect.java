package org.lee.study.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TargetAspect {
//	Pointcut.class, Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class
	@Pointcut("execution(* org.lee.study.aop.Target.print())")
	public void point(){
	}
	@Before("point()")
	public void before(){
		System.out.println("before");
	}

	@After("point()")
	public void after(){
		System.out.println("after");
	}

	@Around("point()")
	public Object round(ProceedingJoinPoint p) throws Throwable {
		System.out.println("round before");
		Object proceed = p.proceed();
		System.out.println("round after");
		return proceed;
	}
	@AfterReturning("point()")
	public void afterReturn(){
		System.out.println("after return");
	}
	@AfterThrowing("point()")
	public void afterThrowing() throws Throwable{
		System.out.println("after throwing");
	}
}
