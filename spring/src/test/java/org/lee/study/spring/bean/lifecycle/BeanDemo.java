package org.lee.study.spring.bean.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

public class BeanDemo implements BeanNameAware, BeanClassLoaderAware, BeanFactoryAware , InitializingBean, DisposableBean{
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("BeanClassLoaderAware   " + classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

        System.out.println("BeanFactoryAware   " + beanFactory);
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("BeanNameAware " + name);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }

    public void init(){
        System.out.println("init");
    }

    public void destroy1(){
        System.out.println("destroy");
    }
    public void destroy(){
        System.out.println("DisposableBean");
    }

}
