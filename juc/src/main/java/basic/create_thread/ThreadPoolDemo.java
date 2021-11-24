package basic.create_thread;

import basic.method.InterruptDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @Classname FutureDemo
 * @Description 线程池创建线程实例
 * @Date 2021/6/22 11:24
 * @Create by Lee
 */
public class ThreadPoolDemo {
	static Logger log = LoggerFactory.getLogger(InterruptDemo.class);

	private final static int MAX_TURN = 5;
	private final static int COMPUTE_TIMES = 100000000;

	// 实际生产不能使用这个Executors，
	// 而是使用
	private static ExecutorService pool = Executors.newFixedThreadPool(3);

	static class DemoThread implements Runnable{
		@Override
		public void run() {
			log.debug(Thread.currentThread().getName()+":线程开始执行");

			for (int i =0;i<MAX_TURN;i++){
				log.debug(Thread.currentThread().getName()+"执行了"+i+"次");
				sleep(10);
			}
			log.debug(Thread.currentThread().getName()+":线程结束执行");

		}
	}
	static class ReturnTask implements Callable<Long> {
		@Override
		public Long call() throws Exception {
			long start  = System.currentTimeMillis();
			log.debug(Thread.currentThread().getName()+":线程开始执行");
			int j = 0;
			for (int i = 0;i<1000000;i++){
				j+=i;
			}
			sleep(1000);
			long end  = System.currentTimeMillis();

			log.debug(Thread.currentThread().getName()+":线程结束执行");
			return end-start;
		}
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		pool.execute(new DemoThread());//执行没有返回值的实例
		pool.execute(()->{
			for (int i =0;i<MAX_TURN;i++){
				log.debug(Thread.currentThread().getName()+"执行了"+i+"次");
				sleep(10);
			}
		});
		Future f = pool.submit(new ReturnTask());
		Object result = f.get();
		log.debug("异步任务执行结果为："+result);
		sleep(Integer.MAX_VALUE);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				System.out.println("??????");
			}};
		Thread thread = new Thread(runnable);

	}
	private static void sleep(int mill){
		try {
			Thread.sleep(mill);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
