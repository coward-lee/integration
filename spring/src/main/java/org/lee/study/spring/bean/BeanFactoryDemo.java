package org.lee.study.spring.bean;

import org.springframework.beans.factory.FactoryBean;

public class BeanFactoryDemo implements FactoryBean<BeanDemo> {

    // 这里定义返回类型
    @Override
    public BeanDemo getObject() throws Exception {
        BeanDemo beanDemo = new BeanDemo();
        beanDemo.setName("aaa");
        return beanDemo;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
