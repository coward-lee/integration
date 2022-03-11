package org.lee.study.aware;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class DisposableBeanDemo implements DisposableBean {
	@Override
	public void destroy() throws Exception {
		System.out.println("DisposableBeanDemo    bean 的destroy");
	}
}
