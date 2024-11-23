package org.lee;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${org.demo:default}")
    String val;
    @Bean
    public String getDemo(){
        System.out.println("value : "+val);
        return val;
    }
}
