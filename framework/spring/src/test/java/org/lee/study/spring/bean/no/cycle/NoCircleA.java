package org.lee.study.spring.bean.no.cycle;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class NoCircleA {
    private final NoCircleB b;

    public NoCircleB getB() {
        return b;
    }

    public void demo() {
        System.out.println("xxx");
    }

    public NoCircleA(@Lazy NoCircleB b) {
        this.b = b;
    }
}
