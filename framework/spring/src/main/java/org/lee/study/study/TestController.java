package org.lee.study.study;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/do")
    public Object doNothing(){
        return "just return a str";
    }

    @GetMapping("/db")
    public Object queryDB(){
        return "return";
    }
}
