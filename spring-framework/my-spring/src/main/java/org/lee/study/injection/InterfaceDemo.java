package org.lee.study.injection;

import org.springframework.stereotype.Service;

@Service
public class InterfaceDemo {
	private final UserDao userDao;

	public InterfaceDemo(UserDao userDao) {
		this.userDao = userDao;
	}
}
