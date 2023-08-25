package org.lee.study.spring.bean.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LifeCycleTest {



    @Test
    void test_demo(){

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Object beanDemo = context.getBean("beanDemo");
        context.close();
        System.out.println(beanDemo);
    }
}
