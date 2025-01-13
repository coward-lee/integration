package basic.create_thread;

import basic.method.InterruptDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @Classname FutureDemo
 * @Description 使用FutureTask+Callable来创建线程实例
 * @Date 2021/6/22 11:24
 * @Create by Lee
 */
public class FutureDemo {
	static Logger log = LoggerFactory.getLogger(InterruptDemo.class);

	static class ReturnTask implements Callable<Long> {
		@Override
		public Long call() throws Exception {
			long start  = System.currentTimeMillis();
			log.debug(Thread.currentThread().getName()+":线程开始执行");
			int j = 0;
			for (int i = 0;i<1000000;i++){
				j+=i;
			}
			Thread.sleep(1000);
			long end  = System.currentTimeMillis();

			log.debug(Thread.currentThread().getName()+":线程结束执行");
			return end-start;
		}
	}

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ReturnTask returnTask = new ReturnTask();
		FutureTask<Long> futureTask = new FutureTask<Long>(returnTask);
		Thread thread = new Thread(futureTask, "returnThread");
		thread.start();


		while(!futureTask.isDone());
		log.debug(String.valueOf(futureTask.get()));

	}
}
