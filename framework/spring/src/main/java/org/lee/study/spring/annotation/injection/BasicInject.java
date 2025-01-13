package org.lee.study.spring.annotation.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasicInject {
    @Autowired
    String demo;
    @Autowired
    int demoInt;
}
