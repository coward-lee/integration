package org.lee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigDemo {


    @Bean
    public String config(DemoService demoService){
        demoService.doSome();
        return "xxx";
    }
}
