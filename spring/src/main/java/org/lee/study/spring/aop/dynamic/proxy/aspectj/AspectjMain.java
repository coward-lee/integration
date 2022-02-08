package org.lee.study.spring.aop.dynamic.proxy.aspectj;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AspectjMain {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("aspectj.xml");
        User user = context.getBean("user", User.class);
        user.add();;
    }
}
