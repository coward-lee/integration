package org.lee.study.spring.circle.prototype;


import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 非singleton测试
 *
 * singleton测试、
 * singleton可以实现依赖循环，
 */
public class NoCircle {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigA.class);
        NoCircleA a = (NoCircleA) context.getBean("noCircleA");
        NoCircleB b = a.getB();
        NoCircleA a1 = b.getA();
        System.out.println(a==a1);
        System.out.println(a1.getB());
    }


}
@Configuration
@ComponentScan(basePackages = "org.lee.study.spring.circle.prototype")
class ConfigA{

}

@Component()
@Scope("prototype")
class NoCircleA{
    private final NoCircleB b;

    public NoCircleB getB() {
        return b;
    }

    public NoCircleA(@Lazy NoCircleB b) {
        this.b = b;
    }
}

@Component()
@Scope("prototype")
class NoCircleB{

    private final NoCircleA a;

    public NoCircleB( @Lazy NoCircleA a) {
        this.a = a;
    }

    public NoCircleA getA() {
        return a;
    }
}