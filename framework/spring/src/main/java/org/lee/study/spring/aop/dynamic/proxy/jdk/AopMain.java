package org.lee.study.spring.aop.dynamic.proxy.jdk;

import java.lang.reflect.Proxy;

public class AopMain {
    public static void main(String[] args) {
        Class[] interfaces = {UserDao.class};

        UserDaoImpl userDaoImpl = new UserDaoImpl();
        UserDao userDao = (UserDao)Proxy.newProxyInstance(AopMain.class.getClassLoader(), interfaces, new UserProxy(userDaoImpl));
        userDao.add(1,2);
        userDao.update(1);
    }
}
