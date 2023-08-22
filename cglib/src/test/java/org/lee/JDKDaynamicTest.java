package org.lee;

import org.junit.jupiter.api.Test;
import org.lee.study.jdk.JdkProxy;
import org.lee.study.jdk.JdkTestDemo;
import org.lee.study.jdk.JdkTestInterface;

import java.lang.reflect.Proxy;

public class JDKDaynamicTest {
    @Test
    void test_jdk() {
        JdkProxy jdkProxy = new JdkProxy(new JdkTestDemo());

        JdkTestInterface o = (JdkTestInterface)Proxy.newProxyInstance(JdkTestDemo.class.getClassLoader(), new Class[]{ JdkTestInterface.class}, jdkProxy);
        o.doSomething();
    }
}
