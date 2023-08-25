package org.lee.study.spring.bean.no.cycle;


import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NoCircle {


    @Test
    void test_demo(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        NoCircleA a = (NoCircleA) context.getBean("noCircleA");
        a.getB().getC().demo();
        a.demo();
    }


}

