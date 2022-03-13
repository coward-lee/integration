package org.lee.study.circle;

import org.lee.study.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

public class CircleDemo {
	public static void main(String[] args) {

		var context = new CustomBeanFactory(Config.class);
//		context.setAllowCircularReferences(true);
//		context.register(Config.class, A.class, B.class, C.class);
		var a = (A) context.getBean("a");
		System.out.println(a.b);
	}

}
class CustomBeanFactory extends AnnotationConfigApplicationContext{

	public CustomBeanFactory() {
		super();
	}

	@SafeVarargs
	public CustomBeanFactory(Class<Config>... configClass) {
		this();
		setAllowCircularReferences(true);
		register(configClass);
		refresh();

	}
}


@Component
class A{
	B b;

	public A(B b) {
		this.b = b;
	}
}

@Component
class B{
	C c;

	public B(@Lazy C c) {
		this.c = c;
	}
}

@Component
class C{
	A a;

	public C(A a) {
		this.a = a;
	}
}