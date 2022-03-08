package org.lee.study.spring.recycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

public class Test {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(Config.class);
        A a = (A) context.getBean("a");
        System.out.println(a.getB());
    }


}
@Configuration
@ComponentScan(basePackages = "org.lee.study.spring.recycle")
class Config{

}

@Component
class A{
    private final B b;

    public B getB() {
        return b;
    }

    public A(@Lazy B b) {
        this.b = b;
    }
}
@Component
class B{

    private final C c;

    public B(@Lazy C c) {
        this.c = c;
    }
}
@Component
class C{

    private final A a;

    public C(@Lazy A a) {
        this.a = a;
    }
}
