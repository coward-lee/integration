package org.lee;

import net.sf.cglib.proxy.Enhancer;
import org.junit.jupiter.api.Test;
import org.lee.study.cglib.TargetInterceptor;
import org.lee.study.cglib.TargetObj;

public class MainCglibTest {
    @Test
    void test_cglib() {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TargetObj.class);
        enhancer.setCallback(new TargetInterceptor());
        TargetObj targetObj = (TargetObj) enhancer.create();
        System.out.println(targetObj);
        System.out.println(targetObj.method1("xxx"));
        System.out.println(targetObj.method2(111));
        System.out.println(targetObj.method3("xxxxxxxx"));
    }
}
