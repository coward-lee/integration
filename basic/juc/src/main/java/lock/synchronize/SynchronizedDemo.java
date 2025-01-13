package lock.synchronize;

import basic.method.InterruptDemo;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

/**
 * @Classname SynchronizedDemo
 * @Description TODO
 * @Date 2021/6/20 15:38
 * @Create by Lee
 */
public class SynchronizedDemo {
	public final static Logger logger = LoggerFactory.getLogger(InterruptDemo.class);
	/**
	 * 输出在jvm中的对象内容
	 */
	@Test
	public void showObjectInfoInJvm() throws InterruptedException {
//		Thread.sleep(5000);
		Object o = new Object();
		int i = 0;
//		System.out.println(GraphLayout.parseInstance(o).toPrintable());
		System.out.println(ClassLayout.parseInstance(o).toPrintable());
		synchronized (o){
//			System.out.println(GraphLayout.parseInstance(o).toPrintable());
			System.out.println(ClassLayout.parseInstance(o).toPrintable());
		}
	}

	@Test
	public void testBatchBiaseLock(){
		Vector<Object> integers = new Vector<>();
		System.out.println("????????????????????????????????????????????????????");
		Thread t1 = new Thread(()->{
			for (int i = 0;i<30;i++){
				Object integer = i;
				integers.add(integer);
				synchronized (integer){
					if (i == 0 || i == 29 || i == 19){
						System.out.println(i+"=======================================\n:"+ClassLayout.parseInstance(integer).toPrintable());
					}
				}

			}
			synchronized (integers){
				integers.notify();
			}
		},"t1");
		t1.start();

		Thread t2 = new Thread(()->{

//			synchronized (integers){
//
//				try {
//					integers.wait();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("=======================================================================================================================");

			for (int i = 0;i<30;i++){
				Object integer = integers.get(i);

				if (i>18 || i == 0 || i == 1){
					System.out.println(i+"\n"+ClassLayout.parseInstance(integer).toPrintable());
				}
				synchronized (integer){
					if (i>18 || i == 0 || i == 1){
						System.out.println(i+"\n"+ClassLayout.parseInstance(integer).toPrintable());
					}
				}
				if (i>18 || i == 0 || i == 1){
					System.out.println(i+"\n"+ClassLayout.parseInstance(integer).toPrintable());
				}

			}
		},"t1");
		t2.start();			try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
