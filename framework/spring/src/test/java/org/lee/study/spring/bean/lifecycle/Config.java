package org.lee.study.spring.bean.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "org.lee.study.spring.bean.lifecycle")
@EnableAspectJAutoProxy
class Config {
    @Bean(initMethod = "init",destroyMethod = "destroy1")
    public BeanDemo beanDemo(){
        return new BeanDemo();
    }

    @Bean
    public  BeanPostProcessorDemo beanPostProcessorDemo(){
        return new BeanPostProcessorDemo();
    }
}
