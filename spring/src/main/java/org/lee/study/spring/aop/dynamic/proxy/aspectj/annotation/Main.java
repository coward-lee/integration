package org.lee.study.spring.aop.dynamic.proxy.aspectj.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.lee.study.spring.aop.dynamic.proxy.aspectj.annotation"})
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(Main.class);
        DemoComponent demoComponent = annotationConfigApplicationContext.getBean("demoComponent", DemoComponent.class);
        demoComponent.test_annotation();
    }


}
