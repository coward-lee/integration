package org.lee.study.listener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ListenerDemo {
	public static void main(String[] args) {
		var context = new AnnotationConfigApplicationContext(Config.class);
		context.publishEvent(new TestEvent("hello", "xxxxxxxxxxxxxxx"));
	}
}
