package org.lee.study.spring.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.lee.study.spring.event")
public class EventCaster {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(EventCaster.class);
        MyEvent myEvent = new MyEvent("hello", "msg");
        context.publishEvent(myEvent);
    }
}
