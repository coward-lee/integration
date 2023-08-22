package org.lee.study.spring.autowired.recycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;


public class BeanDemo {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("org.lee.study.spring.autowired.recycle");
        A bean = context.getBean(A.class);
        System.out.println(bean);
    }
}


@Component
@Scope(SCOPE_PROTOTYPE)
class A{
    @Autowired
    @Lazy
    private B b;
}
@Component
@Scope(SCOPE_PROTOTYPE)
class B{

    @Autowired
    private A a;
}