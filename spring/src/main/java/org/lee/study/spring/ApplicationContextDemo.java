package org.lee.study.spring;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class ApplicationContextDemo {
    public static void main(String[] args) {
        AbstractApplicationContext applicationContext = new AnnotationConfigServletWebApplicationContext();
        applicationContext.refresh();
    }
}
