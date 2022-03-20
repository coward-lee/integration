package org.lee.study.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.lee.study.aware")
public class Test implements BeanFactoryAware {
	private BeanFactory beanFactory;
	int i = 1;
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("BeanFactoryAware Test 。。   "+i++);
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void testAware(){
		((Hello) beanFactory.getBean("hello")).say();
	}

	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(Test.class);
		Test test = (Test) context.getBean("test");
		test.testAware();
	}

}
