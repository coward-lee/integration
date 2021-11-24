package lock.waitandnotify;

import basic.method.InterruptDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Classname WaitAndNotifyDemo01
 * @Description TODO
 * @Date 2021/6/21 13:51
 * @Create by Lee
 */

/**
 * 线程的wait和notify的使用实例01
 * 在没有wait和notify的方法使用情况
 */
public class WaitAndNotifyDemo01 {
	static final Object room = new Object();
	static boolean condition = false;
	static boolean conditionTook = false;
	static Logger log = LoggerFactory.getLogger(InterruptDemo.class);

	public static void main(String[] args) {
		new Thread(()->{
			synchronized (room){
				log.debug("有条件嘛："+condition);
				if (!condition){
					log.debug("没有条件，开始休息");
					sleep(2);
					log.debug("休息完成");

				}
				log.debug("有条件嘛：[{}]",condition);
				if (condition){
					log.debug("{} do it.",Thread.currentThread().getName());
				}
			}
		},"第一个线程").start();

		for (int i = 0;i < 5;i++){
			new Thread(()->{
				synchronized (room){
					log.debug("{}开始干活",Thread.currentThread().getName());
				}
			},"其他人").start();
		}
		sleep(1);
		new Thread(()->{
			condition = true;
			log.debug("条件开始有了");
		}).start();
	}

	public static void sleep(int second){
		try {
			Thread.sleep(second*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
