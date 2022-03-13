package org.lee.study.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

public class TestEvent extends ApplicationEvent {
	String msg;

	public TestEvent(String source) {
		super(source);
	}

	public TestEvent(Object source, String msg) {
		super(source);
		this.msg = msg;
	}
	public void print(){
		System.out.println("msg :"+msg);
	}
}
