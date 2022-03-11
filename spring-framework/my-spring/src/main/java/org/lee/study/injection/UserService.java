package org.lee.study.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * // 这个等同于,其中value等同于bean标签中的id， xml文件中的 <bean id="userService" class="..."></bean>
 * 如果不写value默认为类名的首字母小写
 */
@Component(value = "userService")
public class UserService{
    @Autowired // 根据类型进行注入的
    UserDao userDao;


    @Value(value = "${server.port}")
    private String port;

    public void add(){
        System.out.println("service add... :"+ port);
        userDao.add();
    }
}
