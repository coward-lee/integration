package org.lee.study.spring.same.name;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan(basePackages = "org.lee.study.spring.same.name")
public class SameNameBeanDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SameNameBeanDemo.class);
        Object bean = context.getBean("bean");
        System.out.println(bean);
    }
}

@Component("bean")
class BeanA{

    public void out() {
        System.out.println("bean a");
    }
}
@Component("bean")
class BeanB{

    public void out() {
        System.out.println("bean b");
    }
}
