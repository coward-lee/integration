package org.lee.study.aware;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class InitializingBeanDemo implements InitializingBean {
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("InitializingBeanDemo xxxxxx");
	}
}
