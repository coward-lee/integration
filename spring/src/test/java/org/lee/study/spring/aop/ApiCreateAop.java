package org.lee.study.spring.aop;

import org.springframework.aop.framework.ProxyFactory;

public class ApiCreateAop {

    void test_aop(){
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addInterface(DemoInter.class);

    }
}
