package org.lee.study.injection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 完全注解的方式进行开发
 */
@Configuration     // 替代原来的配置文件
@ComponentScan(basePackages = {"org.lee.study.injection"})
public class SpringConfig {
	@Bean
	public String beanFactoryDemo(){
		return "beanFactoryDemo";
	}

}
