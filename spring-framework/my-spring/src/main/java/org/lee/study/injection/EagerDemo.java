package org.lee.study.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EagerDemo {
	@Autowired
	private UserService userService;
	@Value("${a.b.c:xxxxxx}")
	private String valueTest;
	private UserDaoImpl userDao;

//	public EagerDemo() {
//	}

	@Autowired
	public EagerDemo(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

//	@Autowired
	public EagerDemo(UserService userService, String valueTest, UserDaoImpl userDao) {
		this.userService = userService;
		this.valueTest = valueTest;
		this.userDao = userDao;
	}
}
