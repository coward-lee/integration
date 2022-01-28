package org.lee.study.spring.bean;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

public class BeanMain {

    @Test
    void test(){
        ClassPathResource res = new ClassPathResource("bean1.xml");
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        int i = reader.loadBeanDefinitions(res);
        Object bean_test = factory.getBean("bean_test");
        System.out.println(bean_test);
        bean_test = factory.getBean("bean_test");
        System.out.println(bean_test);
    }
}
