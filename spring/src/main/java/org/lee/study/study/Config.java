package org.lee.study.study;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public UserDemo getBean(){
        return new UserDemo("111",11123);
    }
}
