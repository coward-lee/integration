package design.pattern.guarded_suspension;

/**
 * @Classname GuardedSuspension
 * @Description TODO
 * @Date 2021/6/21 14:41
 * @Create by Lee
 */

import basic.method.InterruptDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 同步模式：
 *  保护性暂停模式
 *      应用在两个线程之间的同步问题
 *
 */
public class GuardedSuspension {
	static Logger logger = LoggerFactory.getLogger(InterruptDemo.class);

	public static void main(String[] args) throws InterruptedException {
		test02();
	}
	public void test01(){
		GuardedObject01 guardedObject = new GuardedObject01();
		new Thread(()->{
			logger.debug("等待结果");
			Object o = guardedObject.get(1000);
			logger.debug("拿到的结果【{}】",o);
		},"测试线程二").start();

		new Thread(()->{
			logger.debug("开始一些动作");
			try {
				Thread.sleep(1001);
				String name = "一些动作一些动作一些动作一些动作";
				guardedObject.complete(name);
				logger.debug("放入内容完成");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		},"测试线程").start();
	}
	public static void test02() throws InterruptedException {
		for (int i = 0;i<3;i++){
			new People().start();
		}
		Thread.sleep(1000);
		for (Integer id: MailBox.getIds()){
			new Postman(id,"内容："+id).start();;
		}
	}
}
