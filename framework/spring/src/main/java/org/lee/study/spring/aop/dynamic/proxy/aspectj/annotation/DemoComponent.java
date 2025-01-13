package org.lee.study.spring.aop.dynamic.proxy.aspectj.annotation;


import org.springframework.stereotype.Component;

@Component
public class DemoComponent implements DemoComponentInter{
    public DemoComponent() {

    }

    @Override
    @StopWatch
    public void test_annotation(){
        try {
            System.out.println("DemoComponent  test_annotation");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
