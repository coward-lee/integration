package org.lee.study.injection;

import org.springframework.stereotype.Service;

@Service
public class EagerDemo {
	UserDaoImpl userDao;

	public EagerDemo(UserDaoImpl userDao) {
		this.userDao = userDao;
	}
}
