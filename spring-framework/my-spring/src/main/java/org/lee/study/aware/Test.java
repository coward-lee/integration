package org.lee.study.aware;

import org.lee.study.Config;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Test implements BeanFactoryAware {
	private BeanFactory beanFactory;
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("BeanFactoryAware Test 有内容注入进来了");
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void testAware(){
		((Hello) beanFactory.getBean("hello")).say();
	}

	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(Config.class);
		Test test = (Test) context.getBean("test");
		test.testAware();

	}

}
