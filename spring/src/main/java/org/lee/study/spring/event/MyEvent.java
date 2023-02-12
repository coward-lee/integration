package org.lee.study.spring.event;


import org.springframework.context.ApplicationEvent;

public class MyEvent extends ApplicationEvent {
    String message;

    public MyEvent(Object source) {
        super(source);
    }

    public MyEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }

    public void print() {
        System.out.println("my event message : " + message);
    }
}
