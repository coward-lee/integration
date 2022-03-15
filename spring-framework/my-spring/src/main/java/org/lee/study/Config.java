package org.lee.study;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration  // 标注为配置类
@ComponentScan("org.lee.study")  // 扫包
//@EnableAspectJAutoProxy  // 开启代理
public class Config {
}
