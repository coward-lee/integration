package org.lee.study.spring.annotation.injection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 基于注解的依赖注入
 * 1、 @Component
 * 2、 @Service
 * 3、 @Controller
 * 4、 @Repository
 *
 * 1、
 * 依赖的需求
 *  注解需要使用到 spring-aop 似乎是在aop里面实现的创建对象
 * 2、 开启组建扫描
 *
 *
 * 基于注解的凡事实现属性注入
 * 1. @Autowired : 根据属性类型进行自动装配
 * 2. @Qualifier : 根据属性名称进行注入，需要和上面的注解Autowired一起使用
 * 3. @Resource  : 可以根据类型或者名称注入
 *
 */
public class AnnotationInjectionDemo {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("annotation.xml");
        UserService userService = context.getBean("userService", UserService.class);
        System.out.println(userService);
        userService.add();
    }
}
