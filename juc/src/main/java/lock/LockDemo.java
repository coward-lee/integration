package lock;

import basic.method.InterruptDemo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

/**
 * @Classname LockDemo
 * @Description TODO
 * @Date 2021/6/20 14:16
 * @Create by Lee
 */
public class LockDemo {

	static Logger logger = LoggerFactory.getLogger(InterruptDemo.class);

	/**
	 * LockSupport.park();的测试
	 */
	@Test
	public void testParkInLockSupport() throws InterruptedException {
		Thread t1 = new Thread(() -> {
			logger.info("parking ");
			LockSupport.park();
			logger.debug("unpark");
			logger.debug("打断状态：{}", Thread.currentThread().isInterrupted());

// 在一次interrupt后在此park线程就不能在此暂停了而是会继续往前执行
//			LockSupport.park();
//			logger.debug("unpark");
// 先要多次park线程生效需要这样
// 调用方法：currentThread().isInterrupted(true);
			logger.debug("打断状态：{}", Thread.interrupted());
			LockSupport.park();
			logger.debug("unpark");
		});
		t1.start();
		Thread.sleep(1000);
		t1.interrupt();;
	}
}
