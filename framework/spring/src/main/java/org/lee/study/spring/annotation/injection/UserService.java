package org.lee.study.spring.annotation.injection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * // 这个等同于,其中value等同于bean标签中的id， xml文件中的 <bean id="userService" class="..."></bean>
 * 如果不写value默认为类名的首字母小写
 */
@Component(value = "userService")
public class UserService{
//    @Autowired // 根据类型进行注入的
//    @Qualifier("userDaoImpl")
//    UserDao userDao;
    // @Resource   // 这样是根据类型进行注入
    @Resource(name = "userDaoImpl") // 根据名称进行注入
    UserDao userDao;

    @Value(value = "${server.port}")
    private String port;

    public void add(){
        System.out.println("service add... :"+ port);
        userDao.add();
    }
}
