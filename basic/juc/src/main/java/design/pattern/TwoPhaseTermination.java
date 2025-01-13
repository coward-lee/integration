package design.pattern;

import basic.method.InterruptDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Classname TwoPhaseTermination
 * @Description
 *          两阶段终止模式
 *          一种较为优雅的终止线程的模式
 *
 * @Date 2021/6/20 14:03
 * @Create by Lee
 */
public class TwoPhaseTermination {
	public static void main(String[] args) throws InterruptedException {
		TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
		twoPhaseTermination.start();;
		Thread.sleep(3500);
		twoPhaseTermination.stop();;
	}
	static Logger logger = LoggerFactory.getLogger(InterruptDemo.class);


	private Thread monitor;
	public void start(){
		monitor = new Thread(
			()->{
				while(true){
					Thread current = Thread.currentThread();
					if (current.isInterrupted()){
						logger.info("料理后事");
						break;
					}
					try {
						Thread.sleep(1000); // 情况一被打断
						logger.info("执行了一次监控");// 情况二被打断

					} catch (InterruptedException e) {
						e.printStackTrace();
						logger.info("在sleep期间被打断了,此时isInterrupt：{}",current.isInterrupted());// 情况二被打断后进入的内容
						current.interrupt();
					}

				}
			}
		);
		monitor.start();
	}
	public void stop(){
		monitor.interrupt();
	}

}
