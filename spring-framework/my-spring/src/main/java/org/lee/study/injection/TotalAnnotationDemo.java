package org.lee.study.injection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 全部基于注解的开发方式
 */
public class TotalAnnotationDemo {
    public static void main(String[] args) {
        // 加载配置类
		ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
//        UserService userService = context.getBean("userService", UserService.class);
//        userService.add();
		Lazy2Demo lazyDemo = context.getBean("lazy2Demo", Lazy2Demo.class);
		lazyDemo.callLazyDemo();
//		lazyDemo.doThing();
	}
}
