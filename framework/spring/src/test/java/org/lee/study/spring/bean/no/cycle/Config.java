package org.lee.study.spring.bean.no.cycle;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = "org.lee.study.spring.bean.no.cycle")
@EnableAspectJAutoProxy
class Config {

}
