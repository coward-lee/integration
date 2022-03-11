package org.lee.study.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@Lazy     // 加载类上的时候是在获取它的第一次才会被初始化
public class LazyDemo {
	private final Lazy2Demo lazy2Demo;


	// 加载构造方法上的作用时机
	@Autowired
	public LazyDemo( Lazy2Demo userDao) {
		this.lazy2Demo = userDao;
	}

	public void doThing(){
		System.out.println("do thing");
	}
}
