package org.lee.study.aop;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("org.lee.study.aop")
@EnableAspectJAutoProxy
//@EnableLoadTimeWeaving // 静态aop 好像需要指定一下代理的jar
public class AopMain {
	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(AopMain.class);
		Target target = context.getBean("target", Target.class);
		target.print();
	}
}
