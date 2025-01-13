package org.lee.study.spring.annotation.injection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 全部基于注解的开发方式
 */
public class TotalAnnotationDemo {
    public static void main(String[] args) {
        // 加载配置类
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        UserService userService = context.getBean("userService", UserService.class);
        System.out.println(userService);
        userService.add();
        BasicInject basicInject = context.getBean("basicInject", BasicInject.class);
        System.out.println(basicInject.demo);
        System.out.println(basicInject.demoInt);
    }
}
