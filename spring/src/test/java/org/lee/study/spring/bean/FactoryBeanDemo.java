package org.lee.study.spring.bean;

import org.springframework.beans.factory.FactoryBean;

public class FactoryBeanDemo implements FactoryBean<BeanDemo> {

    // 这里定义返回类型
    @Override
    public BeanDemo getObject() throws Exception {
        System.out.println("FactoryBeanDemo#getObject");
        BeanDemo beanDemo = new BeanDemo();
        beanDemo.setName("aaa");
        return beanDemo;
    }

    @Override
    public Class<?> getObjectType() {
        System.out.println("FactoryBeanDemo#getObjectType");
        return BeanDemo.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
