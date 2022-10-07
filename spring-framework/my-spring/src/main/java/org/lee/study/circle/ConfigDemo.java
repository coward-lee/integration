package org.lee.study.circle;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.lee.study.circle")
public class ConfigDemo {
	public ConfigDemo() {
		System.out.println("??????????????????");
	}
}
