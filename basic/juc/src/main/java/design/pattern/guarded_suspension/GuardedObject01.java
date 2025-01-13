package design.pattern.guarded_suspension;

/**
 * @Classname GuardedObject01
 * @Description TODO
 * @Date 2021/6/21 14:57
 * @Create by Lee
 */


/**
 * 作为保护性暂停模式的中间类
 * 这里时没有超时问题的解决方案的
 * 在02里面解决它
 */
public class GuardedObject01 {
	private Object respone;

	//获取结果
	public Object get(){
		synchronized (this){
			try {
				while(respone == null){
					this.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return respone;
		}
	}

	//获取结果  等待多久
	// 不能直接使用wait(time);
	public Object get(int outTime){
		long start = System.currentTimeMillis();
		synchronized (this){
			try {
				// 经历的时间
				long exTime = 0;
				long time = 0;

				// 超时问题的解决
				while(respone == null){
					time = outTime-exTime;
					if (time<=0){
						System.out.println("获取超时");
						break;
					}

//					System.out.println("wait time:"+(outTime-exTime));
					this.wait(time);
					exTime = System.currentTimeMillis()-start;

// 超时了，
//					if (outTime<0){
//						System.out.println("获取所失败了");
//						break;
//
//					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return respone;
		}
	}

	public void complete(Object request){
		synchronized (this){
			this.respone = request;
			this.notifyAll();
		}
	}
}
