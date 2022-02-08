package org.lee.study.spring.aop.dynamic.proxy.jdk;

import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl implements UserDao{
    public int add(int a, int b){
        System.out.println("a+b="+a+b);
        return a+b;
    }
    public String update(int id){
        System.out.println("update id:"+id);
        return "xxx"+id;
    }
}
