package basic.method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JoinDemo {
	static Logger logger = LoggerFactory.getLogger(JoinDemo.class);
	static int  r = 10;
	public static void main(String[] args) {
		logger.info("sleep:");
//		testSleep();

		logger.info("=============================:");

		testJoinWithTime();
	}
	/**
	 * join 方法的测试
	 * 用来等待一个线程的·执行结果
	 * 用于线程的同步
	 */
	public static void testJoin(){
		logger.info("start in main.");
		Thread thread = new Thread(() -> {
			logger.info("start in sub-thread");
			try {
				Thread.sleep(1000);
				logger.info("结果");
				r = 100;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			logger.info("t1结束了");
		}, "t1");
		try {
			thread.start();
			thread.join();
			logger.info("结果为：{}",r);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * join 方法的测试
	 * 带参数的join方法
	 * 会根据实际情况的等待时间，即最终等待时间以t.join(time)
	 * 的t为标准
	 */
	public static void testJoinWithTime(){
		logger.info("start in main.");
		Thread thread = new Thread(() -> {
			logger.info("start in sub-thread");
			try {
				Thread.sleep(1000);
				logger.info("结果");
				r = 100;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("t1结束了");
		}, "t1");
		try {
			thread.start();
			thread.join(2000);
			logger.info("结果为：{}",r);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * sleep 方法的测试
	 */
	public static void testSleep(){
		logger.info("start in main.");
		Thread thread = new Thread(() -> {
			logger.info("start in sub-thread");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.info("结果");
			r = 100;
		}, "t1");
		thread.start();

		logger.info("结果为：{}",r);
	}

}
