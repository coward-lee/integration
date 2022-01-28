package org.lee.study.spring.bean;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AnnotatedBeanFactoryDemo {
    public static void main(String[] args) {

        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext("bean3.xml");
//        context.addBeanFactoryPostProcessor((BeanFactoryPostprocessorDemo)context.getBean("beanFactoryPostprocessorDemo"));
        context.refresh();
        Object beanCircle = context.getBean("myBean", Orders.class);
        System.out.println("第四步：获取到bean实例对象");
        System.out.println(beanCircle);
        // 手动销毁创建的Bean实例
        ((ClassPathXmlApplicationContext)context).close();
    }
}
