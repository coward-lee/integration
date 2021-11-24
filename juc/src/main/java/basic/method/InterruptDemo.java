package basic.method;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * suspend
 */
public class InterruptDemo {
	static Logger logger = LoggerFactory.getLogger(InterruptDemo.class);
	public static void main(String[] args) throws InterruptedException {
//		testInterruptWithSleep();
		testInterruptWithRunning();
	}

	/**
	 * 打断一个正在睡眠的时间，interrupt后interrupt的标记会被重新刷新为false
	 * @throws InterruptedException
	 */
	public static void testInterruptWithSleep() throws InterruptedException {
		logger.info("start in main.");
		Thread thread = new Thread(() -> {
			logger.info("start in sub-thread");
			try {
				Thread.sleep(1000);
				logger.info("结果");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			logger.info("t1结束了");
		}, "t1");
		thread.start();
		Thread.sleep(500);
		thread.interrupt();
		logger.info("打断标记{}",String.valueOf(thread.isInterrupted()));
	}
	/**
	 * 打断一个正在运行的线程，interrupt后interrupt的标记《不会》被重新刷新为false
	 * 但是被打断线程还是没有结束执行
	 * @throws InterruptedException
	 */
	public static void testInterruptWithRunning() throws InterruptedException {
		logger.info("start in main.");
		Thread thread = new Thread(() -> {
			logger.info("start in sub-thread");
			while(!Thread.currentThread().isInterrupted()){}
//			logger.info("t1结束了");
		}, "t1");
		thread.start();
		thread.interrupt();
		logger.info("打断标记{}",String.valueOf(thread.isInterrupted()));
	}

}
