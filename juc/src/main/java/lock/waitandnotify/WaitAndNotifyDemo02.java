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
 * 线程测试
 * 将原来的sleep改为wait和notify的情况
 * 单个线程等待的情况
 */
public class WaitAndNotifyDemo02 {
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
					//改变原来的代码
					try {
						// 他会释放锁后面的五个线程会直接执行，因为这里释放了room这个对象的锁
						room.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
			// 改变内容
			synchronized (room){
				condition = true;
				log.debug("条件开始有了");
				room.notify();
			}
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
