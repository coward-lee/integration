package org.lee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Service
@RestController
public class Controller {
    Logger log = LoggerFactory.getLogger(Controller.class);
    @GetMapping("/test")
    public Object test(){
        return "test";
    }
    @GetMapping("/test/wait")
    public Object testWait(){
        log.info("wait请求来了");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("wait请求结束");
        return "testWait";
    }
    @GetMapping("/test/wait/{time}")
    public Object testWaitTime(@PathVariable("time")String time){

        log.info("wait带参数请求结束");
        try {
            int i = Integer.parseInt(time);
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("wait带参数请求结束");
        return "testWait";
    }
    @GetMapping("/test/date/{time}")
    public Object responseDate(@PathVariable("time")String time, HttpServletResponse response){
        response.addHeader("Date", LocalDateTime.now().minusMinutes(1).toString());

        log.info("wait带参数请求结束");
        try {
            int i = Integer.parseInt(time);
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("wait带参数请求结束");
        return "testWait";
    }
}
