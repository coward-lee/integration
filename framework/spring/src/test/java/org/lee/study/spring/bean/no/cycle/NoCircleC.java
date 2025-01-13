package org.lee.study.spring.bean.no.cycle;

import org.springframework.stereotype.Component;

@Component
class NoCircleC {

    private final NoCircleA a;

    public void demo() {
        a.demo();
    }

    public NoCircleC(NoCircleA a) {
        this.a = a;
    }
}
