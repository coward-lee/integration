package org.lee.study.injection;

import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl  implements UserDao {
	SpringConfig springConfig;

	public UserDaoImpl(SpringConfig springConfig) {
		this.springConfig = springConfig;
	}

	public void add(){
        System.out.println("UserDaoImpl add...");

    }
}
