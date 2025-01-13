package org.lee.study.spring.annotation.injection;

import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl  implements UserDao {
    public void add(){
        System.out.println("UserDaoImpl add...");

    }
}
