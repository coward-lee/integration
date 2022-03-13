package org.lee.study.injection;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Lazy
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
