package org.lee.study.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/test")
    public Object test(){
        return "???";
    }
    @GetMapping("/api/sessions")
    public Object test1(){
        return "???";
    }
}
