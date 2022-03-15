package org.lee.study.aop;

import org.springframework.stereotype.Component;

@Component
public class Target  {
	public  void print(){
		System.out.println("print");
	}
}
