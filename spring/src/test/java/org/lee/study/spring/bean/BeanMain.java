package org.lee.study.spring.bean;


import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

class BeanMain {

    @Test
    void test1(){
        ClassPathResource res = new ClassPathResource("bean1.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        int i = reader.loadBeanDefinitions(res);
        Object bean_test = factory.getBean("bean_test");
        System.out.println(bean_test);
        bean_test = factory.getBean("bean_test");
        System.out.println(bean_test);
    }

    @Test
    void test_beanFactory(){
        ClassPathResource res = new ClassPathResource("bean2.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        int i = reader.loadBeanDefinitions(res);
        Object bean_test = factory.getBean("myBean");
        System.out.println(bean_test);
        bean_test = factory.getBean("myBean");
        System.out.println(bean_test);
    }

    /**
     * 生命周期测试
     */
    @Test
    void test_beanCircle(){
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("bean3.xml");
//        context.getAutowireCapableBeanFactory().add
        context.addBeanFactoryPostProcessor((BeanFactoryPostprocessorDemo)context.getBean("beanFactoryPostprocessorDemo"));
        context.refresh();
        Object beanCircle = context.getBean("myBean", Orders.class);
        System.out.println("第四步：获取到bean实例对象");
        System.out.println(beanCircle);
        // 手动销毁创建的Bean实例
        ((ClassPathXmlApplicationContext)context).close();
    }

    @Test
    void test_annotatedBeanFactory(){
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
