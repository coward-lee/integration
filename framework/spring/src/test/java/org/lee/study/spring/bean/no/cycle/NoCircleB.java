package org.lee.study.spring.bean.no.cycle;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class NoCircleB {

    private final NoCircleC c;

    public void demo() {
        System.out.println("xxxx");
    }

    public NoCircleC getC() {
        return c;
    }

    public NoCircleB(@Lazy NoCircleC c) {
        this.c = c;
    }
}
