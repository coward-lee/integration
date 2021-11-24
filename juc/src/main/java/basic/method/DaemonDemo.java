package basic.method;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Classname DaemonDemo
 * @Description TODO
 * @Date 2021/6/20 14:35
 * @Create by Lee
 */
public class DaemonDemo {
	static Logger logger = LoggerFactory.getLogger(InterruptDemo.class);

	@Test
	// 守护线程
	public void testDaemonDemo() throws InterruptedException {
		logger.info("start in main.");
		Thread thread = new Thread(() -> {
			logger.info("start in sub-thread");
			try {
				Thread.sleep(100000);
				logger.info("结果");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}, "t1");
		thread.setDaemon(true);
		thread.start();
		Thread.sleep(500);
		// 设置为守护线程
		logger.info("打断标记{}",String.valueOf(thread.isInterrupted()));
	}
}
