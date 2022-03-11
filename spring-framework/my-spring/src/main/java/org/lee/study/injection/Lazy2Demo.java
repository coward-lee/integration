package org.lee.study.injection;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service

public class Lazy2Demo {
	private final LazyDemo lazyDemo;

	// 加载构造方法上的作用时机
	public Lazy2Demo( @Lazy LazyDemo lazyDemo) {
		this.lazyDemo = lazyDemo;
	}
	public void callLazyDemo(){
		lazyDemo.doThing();
	}
}
