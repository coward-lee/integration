package org.lee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller2 {

    @Autowired
    DemoRepository demoRepository;
    @RequestMapping("/jpa/test")
    public Object demo(){
        Content content = new Content("101", Content.NUMDemo.TEST);
        demoRepository.save(content);
        Content content2 = new Content("102", new Content.ClassDemo("classDemoTest"));
        demoRepository.save(content2);

        System.out.println(demoRepository.findById("101").get());
        System.out.println(demoRepository.findById("102").get());
        return  "xxx";
    }
}
