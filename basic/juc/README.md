[FutureTaskUML]:img/FutureTaskUML.png
[ScheduledThreadPoolExecutor]:img/ScheduledThreadPoolExecutor.png
[synchronized_basic]:img/synchronized_basic.jpg
[CHL_theory]:img/CHL_theory.jpg
[getLockReentrantLock]:img/getLockReentrantLock.png




# 1.多线程   (线程、线程池、ThreadLocal)
## 1.1 多线程
* 工具:jstack : jstack <pid> # pid进程id
* jps # 可以用来查看**java**的进程id
### 1.1.1 创建线程的四种方式
>
    TIP：无论哪种方式最终都是实现Runnable接口 <br>
    1.继承Thread类，基本就不演示了 <br>
    2.实现Runnable接口，先实现Runnable接口在通过Thread的构造函数传入Runnable  <br>
    3.Callable和FutureTask
    Callable<V>：它支持返回值和抛出异常Runnable都不支持
        public interface Callable<V> {
            V call() throws Exception;
        }
    Futuretask的核心重要点，他run方法的实现
```java
public class FutureTask<V> implements RunnableFuture<V> {
    public void run() {
        try {
            Callable<V> c = callable;
            if (c != null && state == NEW) {
                V result;
                boolean ran;
                try {
// 核心这里的run还是调用了Callable的call方法来接受返回值，并把返回值放到result的成员变量中(这里的成员变量为一个泛型)
                    result = c.call();
                    ran = true;
                } catch (Throwable ex) {
                    result = null;
                    ran = false;
                    setException(ex);
                }
                if (ran)
                    set(result);
            }
        } finally {
        }
    }
}
```
         
    图FutureTaskUML如下，注意他的最终的基类接口还是用到了Runnable所以做种他能放到一个线程里面执行
>   ![FutureTaskUML]
    <center> 图future task </center>
```java
// 对应的一个Demo
    public class FutureDemo {
    // 1. 实现Callable<T>接口
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
// 2. 实例化
            ReturnTask returnTask = new ReturnTask();
// 3.将Callable放入Future接口下的实现类FutureTask类下面
            FutureTask<Long> futureTask = new FutureTask<Long>(returnTask);
// 4.再将Future放入THread中
            Thread thread = new Thread(futureTask, "returnThread");
            thread.start();
    
            while(!futureTask.isDone());
    
        }
    }
```
>
    4.线程池创建线程
```java
public class ThreadPoolDemo {
// 1.创建一个固定数量的线程池，（注意在实际开发中不会这么创建线程池而是使用ThreadPoolExecutor，来显式指定线程的各种参数）
/**
                              int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler
*/

	private static ExecutorService pool = Executors.newFixedThreadPool(3);

// 2.实现一个Runnable接口，用于一会儿放入线程池，这个是用于实现不带返回值的线程池任务
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
// 3.实现一个Callable<T>接口，用于执行需要返回数据的线程池任务
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
// 主函数
	public static void main(String[] args) throws ExecutionException, InterruptedException {
// 4.执行没有返回值的实例execute方法
        pool.execute(new DemoThread());//执行没有返回值的实例
		pool.execute(()->{
			for (int i =0;i<MAX_TURN;i++){
				log.debug(Thread.currentThread().getName()+"执行了"+i+"次");
				sleep(10);
			}
		});
// 5.执行没有返回值的实例submit方法
		Future f = pool.submit(new ReturnTask());
// 6.异步获取Future任务
		Object result = f.get();
		log.debug("异步任务执行结果为："+result);
		sleep(Integer.MAX_VALUE);
	}
}
```
### 1.1.2 线程的一些原理
> 
    线程调度模型
        时片间轮转：每个线程执行固定时间
        抢占式系统按照优先级分配CPU的时间片，java中使用Thread的成员变量priority（默认为5，最小1，最大10）来设置时间片，但是这个优先级可能不会生效
    线程的生命周期
```java
 public enum State {
        /**
         * Thread state for a thread which has not yet started.
         * 线程新创建还未有开始,新new还没有调用start方法
         */
        NEW,

        /**
         * Thread state for a runnable thread.  A thread in the runnable
         * state is executing in the Java virtual machine but it may
         * be waiting for other resources from the operating system
         * such as processor.
         * 线程的可运行态，
         * 1. 正在运行
         * 2. 正在等待一些资源如处理器
         */
        RUNNABLE,

        /**
         * Thread state for a thread blocked waiting for a monitor lock.
         * A thread in the blocked state is waiting for a monitor lock
         * to enter a synchronized block/method or
         * reenter a synchronized block/method after calling
         * {@link Object#wait() Object.wait}.
         * 阻塞态：等待一个monitor锁进入synchronized的代码块或者方法或者在代码块中调用了wait方法，
         * 即：  1.没有抢到synchronized的所资源
         *      2.在synchronized代码块/方法中调用了wait方法
         */
        BLOCKED,

        /**
         * Thread state for a waiting thread.
         * A thread is in the waiting state due to calling one of the
         * following methods:
         * <ul>
         *   <li>{@link Object#wait() Object.wait} with no timeout</li>
         *   <li>{@link #join() Thread.join} with no timeout</li>
         *   <li>{@link LockSupport#park() LockSupport.park}</li>
         * </ul>
         *
         * <p>A thread in the waiting state is waiting for another thread to
         * perform a particular action.
         *
         * For example, a thread that has called {@code Object.wait()}
         * on an object is waiting for another thread to call
         * {@code Object.notify()} or {@code Object.notifyAll()} on
         * that object. A thread that has called {@code Thread.join()}
         * is waiting for a specified thread to terminate.
         * 等待状态
         * 1.一个线程的锁对象调用了，Object#wait() Object.wait(),不带时间参数，可以通过对象调用notify/notifyAll方法唤醒，
         * 2.线程调用了join()，在等待被并入的线程执行完了就会继续执行，就是等想一个线程执行完成后再运行
         * 3.LockSupport.park()使用了这个方法，通过LockSupport.unpark()唤醒
         */
        WAITING,

        /**
         * Thread state for a waiting thread with a specified waiting time.
         * A thread is in the timed waiting state due to calling one of
         * the following methods with a specified positive waiting time:
         * <ul>
         *   <li>{@link #sleep Thread.sleep}</li>  
         *   <li>{@link Object#wait(long) Object.wait} with timeout</li>
         *   <li>{@link #join(long) Thread.join} with timeout</li>
         *   <li>{@link LockSupport#parkNanos LockSupport.parkNanos}</li>
         *   <li>{@link LockSupport#parkUntil LockSupport.parkUntil}</li>
         * </ul>
         * 1. 使用了Thread.sleep(time);方法
         * 2. 带时间的wait、join和parkNanos
         * 3. LockSupport的parkUntil方法
         */
        TIMED_WAITING,

        /**
         * Thread state for a terminated thread.
         * The thread has completed execution.
         * 执行结束
         */
        TERMINATED;
    }
```    
### 1.1.3 线程的基本操作
* 设置获取名称 
* sleep() 
* interrupt() (优雅的停止线程的做法, 两阶段终止模式) 
* join() 合并线程，等待另一个线程执行完成后在执行自己
* yield() 让出时间片
* daemon 守护线程
```java
/**
 *          两阶段终止模式
 *          一种较为优雅的终止线程的模式
 */
class TwoPhaseTermination{
	public void start(){
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
}

```
## 1.2 线程池
### 1.2.1 ScheduledThreadPoolExecutor的UML
![ScheduledThreadPoolExecutor]
### 1.2.2 四种线程池创建方法(不推荐使用)
* 
    1. newFixedThreadPool 固定数量的线程池
        方法代码：
        public static ExecutorService newFixedThreadPool(int nThreads) {    <br>
            return new ThreadPoolExecutor(nThreads, nThreads,     <br>
                                          0L, TimeUnit.MILLISECONDS,  <br>
                                          new **LinkedBlockingQueue**<Runnable>()); <br>
        }

        阻塞队列使用的LinkedBLockQueue，这个队列可以无限长度，如果等待线程过多就可能造成内存溢出（OOM）
    2. newSingleThreadExecutor 单线程的线程池

        方法对应的代码：
            public static ExecutorService newSingleThreadExecutor() { <br>
                return new FinalizableDelegatedExecutorService       <br>
                    (new ThreadPoolExecutor(1, 1,                      <br>
                                            0L, TimeUnit.MILLISECONDS,   <br>
                                            new **LinkedBlockingQueue**<Runnable>()));   <br>
        同1，它的事项激素hi在固定数量上面置**1**
    3. newScheduledThreadPool 可定期或者延时的线程池
    4. newCachedThreadPool 不限制数量的线程池
        public static ExecutorService newCachedThreadPool() {  <br>  
            return new ThreadPoolExecutor(0,  //       <br>
                                          **Integer.MAX_VALUE**,  <br>
                                          60L,                      <br>
                                          TimeUnit.SECONDS,            <br>
                                          new **SynchronousQueue**<Runnable>());           <br>
        这个主要是最大线程数的参数（maximumPoolSize）为：Integer.MAX_VALUE，阻塞队列使用的是，所以可能造成内存溢出（OOM）
### 1.2.3 线程池创建的标准方式（ThreadPoolExecutor）
```java
public class ThreadPoolExecutor extends AbstractExecutorService {
// 核心构造方法
    public ThreadPoolExecutor(int corePoolSize,  // 核心线程数
                                  int maximumPoolSize, // 线程池的线程数量上限，这个会加上等待队列中的线程
                                  long keepAliveTime, // 线程最大空闲时间
                                  TimeUnit unit,  // 时间单位
                                  BlockingQueue<Runnable> workQueue, // 线程的队列
                                  ThreadFactory threadFactory,  // 线程工厂，即创建线程的方式
                                  RejectedExecutionHandler handler // 拒绝策略
        ) {
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
}
// 线程工厂 专门用于模板化创建线程的一个类，
public interface ThreadFactory {
    Thread newThread(Runnable r);
}
```
### 1.2.4 线程池提交任务的方式
* 
    1. execute(Runnable) 执行不返回内容的任务
    2. submit(Callable<V>) 执行返回内容的任务，这个需要一个Future<V>来接收submit的返回结果/处理异常
### 1.2.5 线程池的调度流程
*
    1. 任务提交
    2. 检查corePoolSize是否满了；没有满（此时不管有没有空闲线程）：创建一个线程开始执行任务
    3. 满了：再判断线程成队列是否满；没有满：进入等待队列
    4. 满了：再判断是否maximumPoolSize是否已满；没有满：是执行拒绝策略
    5. 创建线程执行任务？？？ 
### 1.2.6 任务阻塞队列
* 这个后面的JUC容器的一章单独讲解
    1. ArrayBlockingQueue (数组阻塞队列)，默认大小为Integer.Max_VALUE
    2. LinkedBLockQueue (链式阻塞队列) 
    3. PriorityBlockingQueue<E> (优先级阻塞队列)，这个对有优先级的排序      
        成员变量，还是用的数组来实现的private transient Object[] queue;h
    4. DelayQueue(延时队列)，基于PriorityBlockingQueue，不过多了过期时间的限制，过了限制时间才能出队
    5. SynchronousQueue (同步队列)， 有点不懂 todo
### 1.2.7 线程池的回调方法
*
    1. void beforeExecute(Thread t, Runnable r) 执行前
    2. void beforeExecute(Runnable r, Throwable t) 执行后
    3. void terminated() 
>
    beforeExecute beforeExecute的实现
     ...
    beforeExecute(wt, task);
    try {
        task.run();
        afterExecute(task, null);
    } catch (Throwable ex) {
        afterExecute(task, ex);
        throw ex;
    }
    ...
    terminated的回调实现：
        tryTerminate(){
        ...
        if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
            try {
                terminated();
            } finally {
                ctl.set(ctlOf(TERMINATED, 0));
                termination.signalAll();
            }
            return;
        }
        ...
        }
    tryTerminate 在purge、remove、shutdownNow、shutdown、processWorkerExit、addWorkerFailed中被调用
### 1.2.8 线程池的拒绝策略
*  interface RejectedExecutionHandler
    1. AbortPolicy  拒绝策略:这哥们儿抛异常
        throw new RejectedExecutionException("Task " + r.toString() +
                                                     " rejected from " +
                                                     e.toString());
    2. CallerRunsPolicy   调用者执行策略,给提交任务的执行者执行此任务
        if (!e.isShutdown()) {
                        r.run();
        }
    3. DiscardOldestPolicy 抛弃最老的策略
    4. DiscardPolicy   抛弃策略:什么都不执行，放弃的执行
        Does nothing, which has the effect of discarding task r.
        
### 1.2.9 线程池的关闭
```java
class ShutdownThreadPool{
    public static void shutdownThreadPoolGracefully(ExecutorService pool){
// 1. 检查是否关闭，关闭了直接返回
        if (pool.isShutdown()){
            return;
        }
// 2. 调用shutdown此后不再接受新的任务
        try{
            pool.shutdown();
        }catch (SecurityException e){
            return;
        }catch (NullPointerException e){
            return;
        }
        try{
// 3. 等待60秒，等待线程池中的线程执行完成
            if (!pool.awaitTermination(60,TimeUnit.SECONDS)){
// 4. 超过规定时间后取消正在执行的任务
                pool.shutdownNow();
// 5. 再次等待60秒如果还没有结束，就可以直接放弃
                if (!pool.awaitTermination(60,TimeUnit.SECONDS)){
                    System.err.println("线程池任务未正常结束");
                }
            }
        } catch (InterruptedException e) {
// 出现异常就需要手动结束咯
            pool.shutdownNow();
            return;
        }
// 6. 判断线程池是否关闭，循环1000次，每次间隔10毫秒
        if (!pool.isTerminated()){
            try{
                for (int i = 0; i < 1000; i++){
                    if (pool.awaitTermination(10,TimeUnit.SECONDS)){
                        return;
                    }
                    pool.shutdownNow();
                }
            }catch (InterruptedException e){
                System.err.println(e.getMessage());
            }
        }
    }
}
```
> 这里还有一个 JVM关闭线程池的回调函数
```java
class Demo{    public void demo(){
    Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("关闭线程池的线程", new Callable<Object>() {
        @Override
        public Object call() throws Exception {
            shutdownThreadPoolGracefully(EXECUTOR);
            return null;
        }
    }));
} }
```
## 1.3 ThreadLocal(线程本地变量)

### 1.3.1 旧版和最新版的存储方式
* 旧版：JDK1.8之前
>
    采用的是 一个ThreadLocal中存放的是
        Key（Thread）：Value（T extends Object）
    其中Key为线程，Value为具体的线程里面的本地变量
* 新版：JDK1.8之后
```java
// 线程的类成员中有一个ThreadLocalMap的threadLocals对象
    class Thread{
        // ...
        ThreadLocal.ThreadLocalMap threadLocals = null;
        // ...
    }

// 线程ThreadLocal有是对ThreadLocalMap进行维护的类
// ThreadLocalMap又是一个ThreadLocal的内部类，
    public class ThreadLocal<T> {
        // ...
// 这里的set和get是以自己为Key进行操作
        public void set(T value) {
// 1. 获取线程的ThreadLocalMap
            Thread t = Thread.currentThread();
            ThreadLocalMap map = getMap(t);
// 2. 并以自己的实例对象为Key放入数据
            if (map != null) {
                map.set(this, value);
            } else {
                createMap(t, value);
            }
        }
// get同理
        get();
        static class ThreadLocalMap {
            static class Entry extends WeakReference<ThreadLocal<?>> {
                /** The value associated with this ThreadLocal. */
                Object value;
    
                Entry(ThreadLocal<?> k, Object v) {
                    super(k);
                    value = v;
                }
            }
        }
    }
```
### 1.3.2 ThreadLocalMap的initialValue方法

```java

public class ThreadLocal<T> {
// 默认放回空的初始化值
    protected T initialValue() {
        return null;
    }
// 但是它提供了一个初始化的工厂方法
    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal<>(supplier);
    }
}
// Supplier接口可以传入一个lambda表达式
public interface Supplier<T> {
    T get();
}
// 通过Supplier接口的get返回的值来实现对value的初始化
static final class SuppliedThreadLocal<T> extends ThreadLocal<T> {
    private final Supplier<? extends T> supplier;

    SuppliedThreadLocal(Supplier<? extends T> supplier) {
        this.supplier = Objects.requireNonNull(supplier);
    }

    @Override
    protected T initialValue() {
        return supplier.get();
    }
}
// 一个Demo,:: 调用静态方法
// ThreadLocal<T> fooThreadLocal = ThreadLocal.withInitial(T::getT);

```


### 1.3.2 ThreadLocal的应用场景
1. 线程隔离,如数据库连接池独享和session的管理
2. 

# 2.Java内置锁(synchronized关键字) 

## 2.1 使用及作用域
1. 添加在代码块上，锁的是实例对象，就是synchronized(obj)中的obj对象（可以是多个对象）
2. 添加在非静态方法，锁的调用此方法的实例对象
3. 添加在静态方法上面，锁的是整个类
        
## 2.1 Java对象结构
堆区内存
1. 一个实例对象的结构如下：        
>
    Mark Word  (32/64 bit)
    Class Pointer（指向方法区内存的Class对象，这个默认开启了UseCompressedOops将原来的64bit地址压缩到了32bit）
    Array Length(可选，如果该对象是数组，则存在，32bit)
    Field1
    Field2
    对齐字节
2. 方法区内存
>
    类的Class对象
    类的Class对象
    类的Class对象

UseCompressedOops将下列指针将64位压缩到了32位               <br>
Class对象的属性执行（静态变量）       <br>
Class对象的属性执行（成员变量）       <br>
不同对象的数组的元素指针              <br>

MarkWord的几个锁状态的情况
![synchronized_basic]
## 2.3 四个阶段
1. 无锁态：表示的是此时还没有线程使用使用此对象，仅仅是实例化了，此时的mark word 为：25位未用、31位对象的hashcode、1位未使用、4位age、后三位：0、01
2. 偏向锁：这个时候只有一个线程占用了此对象，此时对象结构中的mark word： 前面54位为线程的ID、2位epoch、1位未使用、后面三位：1、01 
    
    这个偏向锁在获取锁是是通过cas实现的，而cas本身不是原子操作（指令为 cmpxchg），但是synchronized关键字的cas的指令前面多了一个**lock**指令前缀，使得cas的过程称为原子性的操作，后面的轻量级锁的cas同样也是一样的原理，
      
    当前面的54位的线程ID为自己的时候线程可以<font color="red">重入</font>，这个也是为什么锁CAS是可重入的锁

3. 轻量级锁：这个是有两个或者以上的线程在竞争这个锁对象，此时的mark word：62位为锁记录指针、后两位 00
    * 同时在jdk1.6之后使用了自适应自旋，大概意思是以前拿到过就自旋久一点，没有拿到过就自旋时间短一点。

4. 重量级锁：此时由于轻量级锁竞争过于激烈锁膨胀为重量级中（基于mutex lock互斥锁机制实现），mark word：62位为monitor对象指针， 后两位：10        
    重量级锁开销问题，由于需要切换进程的运行状态所需消耗很大
    * 同步操作交给了我操作系统实现，这里的等待线程就会进入等待队列不会再消耗CPU了（在等待时候不会消耗）
    * _WaitSet、cxq、_EntryList，这三个队列存放抢夺重量级锁的线程，及synchronized进入重量级之后线程会被放置在这三个队列之中
    * cxq请求锁的线程首先会被放在这个队列中，这里有个ABA问题是，因为他的竞争模式还是利用的CAS,他是在对头插入，队尾取出（这样来避免ABA问题，没有懂todo）：详细在JAva高并发核心编程 卷2 164页
    * EntryList Cxq中那些符合资格的线程会被移动到EntryList(什么是符合资格的怎么计算或者统计的)  todo 详细在JAva高并发核心编程 卷2 164页
    * WaitSet 在ObjectMonitor中调用了wait的之后的会被放入到这个里面 详细在JAva高并发核心编程 卷2 165页
    * 以下的一些队列可以证明：在调用notify/notifyAll的时候具体的操作是WaiSet中的第一个/全部线程被放到EntryList队列中
```c++
 // monitor结构体 cpp代码
    MonitorObject::ObjectMonitor(){
        _header = NULL;
        _counter = 0;
        _waiters = 0;
        
        // 线程重入次数
        _recursions = 0;
        _object = NULL;
        
        // 表示拥有该Monitor的线程,就是设获取到了该Monitor对象
        _owner = NULL;
        
        // 等待线程组成的双向循环队列
        _WaitSet = NULL;
        _WaitSetLock = 0;
        _Responsible = NULL;
        _succ = NULL;
        
        // 多线程竞争锁进入时的单向链表
        cxq = NULL;
        FreeNext = NULL;
        
        // _owner从双向链表中唤醒的线程节点
        _EntryList = NULL;
        _SpinFreq = 0;
        _SpinLock = 0;
        OwnerIsThread = 0;
    }
```
  
## 2.4 线程间的通信
这个会有几个问题，低效的通信、虚假唤醒等问题

```java
class Demo{
    //获取结果这里有一个防止错误唤醒的小问题
    	public Object get(){
    		synchronized (this){
    			try {
    			//错误唤醒的小问题解决方案
    				while(respone == null){
    					this.wait();
    				}
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			return respone;
    		}
    	}
    // 通知唤醒
    public void complete(Object request){
    		synchronized (this){
    			this.respone = request;
    			this.notifyAll();
    		}
    	}
}

```

# 3. Cas原理与JUC原子类

## 3.1 Unsafe和Unsafe#CAS(Unsafe::CAS)
获取Unsafe对象，通过反射
>
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
三个和核心的cas方法，该方法的四个操作数：字段所在对应的对象，字段内存位置，预期原值，期望值
```java
public final class Unsafe {
    @HotSpotIntrinsicCandidate
    public final native boolean compareAndSetLong(Object o, long offset,
                                                 long expected,
                                                 long x);
    @HotSpotIntrinsicCandidate
    public final native boolean compareAndSetObject(Object o, long offset,
                                                    Object expected,
                                                    Object x);
    @HotSpotIntrinsicCandidate
    public final native boolean compareAndSetInt(Object o, long offset,
                                                 int expected,
                                                 int x);
    // 获取字段偏移量,这个需要结合第二章里面的对象结构来一起理解可能更加清楚：
    // 这里的static字段是存放在方法区里面的class对象里面，而不是堆区的实例对象里面
    public long staticFieldOffset(Field f) {
        if (f == null) {
            throw new NullPointerException();
        }

        return staticFieldOffset0(f);
    }
    private native long staticFieldOffset0(Field f);

    public long objectFieldOffset(Field f) {
        if (f == null) {
            throw new NullPointerException();
        }

        return objectFieldOffset0(f);
    }
    private native long objectFieldOffset0(Field f);
 }
```

## 3.2 JUC的原子类
* Atomic原子操作包,juc.atomic
>
    基本原子类型： 
    AtomicBoolean
    AtomicInteger
    AtomicLong
    数组原子类型： 数组的原子类型并不是说，一次性操作整个数组是原子的，而是说对其中的某个元素操作是原子的。
    AtomicLongArray
    AtomicIntegerArray
    AtomicReferenceArray<E> 引用类型数组原子类
    引用原子类，后面两个都可以用来解决ABA问题，
    AtomicReference<V>
    AtomicStampedReference<V>   带更新版本的
        解决ABA问题：
```java
public class AtomicStampedReference<V> {
    
    private static class Pair<T> {
        final T reference;
        final int stamp;
        private Pair(T reference, int stamp) {
            this.reference = reference;
            this.stamp = stamp;
        }
        static <T> Pair<T> of(T reference, int stamp) {
            return new Pair<T>(reference, stamp);
        }
    }
    // 引用对象成员变量这个是实现版本的关键
    private volatile Pair<V> pair;

    public AtomicStampedReference(V initialRef, int initialStamp) {
        pair = Pair.of(initialRef, initialStamp);
    }

    public boolean compareAndSet(V   expectedReference,  // 预期的引用
                                 V   newReference, // 新的引用
                                 int expectedStamp,  // 预期的戳
                                 int newStamp) { // // 新的戳
        Pair<V> current = pair;           // 当前的引用对象
        return
            expectedReference == current.reference &&  // 预期引用与当前（就是现在这个引用原子类对应的实例对象的当前的引用）的引用是否一致
            expectedStamp == current.stamp      // 预期戳与当前的戳是否一致
                &&                              // 前面两个逻辑与，再与后面的整体结果逻辑与     
            ((newReference == current.reference && // 新的引用与当前的引用比较
              newStamp == current.stamp)          // 当新戳与当前戳比较  
                ||                              // 前面两个逻辑与，再与后面的casPair逻辑或
             casPair(current, Pair.of(newReference, newStamp)));
    }
    private static final VarHandle PAIR;
// compareAndSet调用的cas方法，PAIR又是一个VarHandle，又可以说是一个类似与Unsafe的类，调用的cas方法是变参的方法
    private boolean casPair(Pair<V> cmp, Pair<V> val) {
        return PAIR.compareAndSet(this, cmp, val);
    }
    // ...
}
public abstract class VarHandle {

    /**  注释有空翻译一下
     * Atomically sets the value of a variable to the {@code newValue} with the
     * memory semantics of {@link #setVolatile} if the variable's current value,
     * referred to as the <em>witness value</em>, {@code ==} the
     * {@code expectedValue}, as accessed with the memory semantics of
     * {@link #getVolatile}.
     *
     * <p>The method signature is of the form {@code (CT1 ct1, ..., CTn ctn, T expectedValue, T newValue)boolean}.
     *
     * <p>The symbolic type descriptor at the call site of {@code
     * compareAndSet} must match the access mode type that is the result of
     * calling {@code accessModeType(VarHandle.AccessMode.COMPARE_AND_SET)} on
     * this VarHandle.
     *
     * @param args the signature-polymorphic parameter list of the form
     * {@code (CT1 ct1, ..., CTn ctn, T expectedValue, T newValue)}
     * , statically represented using varargs.
     * @return {@code true} if successful, otherwise {@code false} if the
     * witness value was not the same as the {@code expectedValue}.
     * @throws UnsupportedOperationException if the access mode is unsupported
     * for this VarHandle.
     * @throws WrongMethodTypeException if the access mode type does not
     * match the caller's symbolic type descriptor.
     * @throws ClassCastException if the access mode type matches the caller's
     * symbolic type descriptor, but a reference cast fails.
     * @see #setVolatile(Object...)
     * @see #getVolatile(Object...)
     */


    public final native
    @MethodHandle.PolymorphicSignature
    @HotSpotIntrinsicCandidate
    boolean compareAndSet(Object... args);
}
```
    AtomicMarkableReference<V>  带更新标记的
    字段更新原子类
    AtomicReferenceFieldUpdater
    AtomicLongFieldUpdater
    AtomicIntegerFieldUpdater

## 3.3 LongAdder(空间换取时间的思想来优化并发)
long型累加其，再超大规模累加的时候是单体累加的差不多8倍的效率，单个的时候效率差球不大

```java

public class LongAdder extends Striped64 implements Serializable {
    public void add(long x) {
        Cell[] cs; long b, v; int m; Cell c;
        // 判断是否cells为空或者基础的累加失败了进入if
        // 如果cells为空或者基础累加成功了都不会进入if
        // 进入if就是为了开始进行cells里面的累加
        if ((cs = cells) != null ||   // cells 不为空 
                !casBase(b = base, b + x)) {  // 或者基础累积失败 则进入if
            boolean uncontended = true;  // 
            if (cs == null ||   // 再判断cs是否为空  
                    (m = cs.length - 1) < 0 || // cs的长度是否大于 1
                (c = cs[getProbe() & m]) == null ||   // 获取线程里面的一个东西
                !(uncontended = c.cas(v = c.value, v + x))) // cas cells的元素
                longAccumulate(x, null, uncontended); // 调用父类的累加方法
        }
    }

    private static final VarHandle THREAD_PROBE;
    static final int getProbe() {
        return (int) THREAD_PROBE.get(Thread.currentThread());
    }
}
// 但是其核心的内容在其继承的Striped64类上面
abstract class Striped64 extends Number {
        /**
         * Table of cells. When non-null, size is a power of 2.
         * 
         */
        transient volatile Cell[] cells;
    
        /**
         * Base value, used mainly when there is no contention, but also as
         * a fallback during table initialization races. Updated via CAS.
         基础的值，没有竞争的时候或者在cells不可用的时候可以操作此值
         */
        transient volatile long base;
    
        /**
         * Spinlock (locked via CAS) used when resizing and/or creating Cells.
         * 自旋锁的标识，判断是否cells在扩容或者初始化
         */
        transient volatile int cellsBusy;
    final void longAccumulate(long x, LongBinaryOperator fn,
                              boolean wasUncontended) {
        int h;
        if ((h = getProbe()) == 0) {
            ThreadLocalRandom.current(); // force initialization
            h = getProbe();
            wasUncontended = true;
        }
        boolean collide = false;                // True if last slot nonempty
        done: for (;;) {
            Cell[] cs; Cell c; int n; long v;
            if ((cs = cells) != null && (n = cs.length) > 0) { 
// if—1 开始
                if ((c = cs[(n - 1) & h]) == null) { 
// if-2 开始                
                    if (cellsBusy == 0) {       // Try to attach new Cell
                        Cell r = new Cell(x);   // Optimistically create
                        if (cellsBusy == 0 && casCellsBusy()) {
                            try {               // Recheck under lock
                                Cell[] rs; int m, j;
                                if ((rs = cells) != null &&
                                    (m = rs.length) > 0 &&
                                    rs[j = (m - 1) & h] == null) {
                                    rs[j] = r;
                                    break done;
                                }
                            } finally {
                                cellsBusy = 0;
                            }
                            continue;           // Slot is now non-empty
                        }
                    }
                    collide = false;
                }
// if-2 的if结束               
                else if (!wasUncontended)       // CAS already known to fail
                    wasUncontended = true;      // Continue after rehash
                else if (c.cas(v = c.value,
                               (fn == null) ? v + x : fn.applyAsLong(v, x)))
                    break;
                else if (n >= NCPU || cells != cs)
                    collide = false;            // At max size or stale
                else if (!collide)
                    collide = true;
                else if (cellsBusy == 0 && casCellsBusy()) {
                    try {
                        if (cells == cs)        // Expand table unless stale
                            cells = Arrays.copyOf(cs, n << 1);
                    } finally {
                        cellsBusy = 0;
                    }
                    collide = false;
                    continue;                   // Retry with expanded table
                }
// 整个if-2结束，包括else if的内容
                h = advanceProbe(h);
            }
// if-1 的if 结束
            else if (cellsBusy == 0 && cells == cs && casCellsBusy()) {
// if-1 的else-if 开始
                try {                           // Initialize table
                    if (cells == cs) {
                        Cell[] rs = new Cell[2];
                        rs[h & 1] = new Cell(x);
                        cells = rs;
                        break done;
                    }
                } finally {
                    cellsBusy = 0;
                }
            }
            // Fall back on using base
            else if (casBase(v = base,
                             (fn == null) ? v + x : fn.applyAsLong(v, x)))
                break done;
        }
    }


}
```


# 4. 可见性与有序性(volatile关键字)

## 4.1 可见性与有序性问题
    可见性：
        volatile关键的内存结构
        内存：
            volatile变量
            线程内存（工作内存）：
                volatile变量副本
            线程内存（工作内存）：
                volatile变量副本
    有序性：
        此时，再单线程的情况下三句话可任意切换而不会出现问题
        但是多线程情况下就可能出现问题了
        a = 0;
        i++;
        b = 0;
## 4.2 MESI协议
    mesi 缓存一致性协议
        总线锁:锁住总线让其他处理器无法访问缓存，主存，北桥等
        缓存锁：当对该缓存的内容操作完后，JVM会向CPU发送一条lock指令将缓存中的内容写入主存，而其他的CPU会通过嗅探总线上的数据来判断自己的总线是否已经失效，
            写入主存有两种模式： 直接，有数据更新直接写人主存，
            回写： 会不直接写入主存，而是只写入高速缓存，只有再数据被替换出高速缓存的或者变成共享状态的时候，且数据有变动才会将数据更新到主存。
## 4.3 有序性与内存屏障
* lock 指令的功能，具有全屏障功能
1. 将当前CPU缓存行的数据立即写回系统内存，
2. 会引起其他CPu中的缓存了该内存地址的数据无效
3. 禁止指令重排
### 4.3.1 重排序
    重排序：    
        编译器重排，再java文件到class文件的时候
        cpu重排序，指令重排，内存系统重排，主要是由于引入了流水线的问题
### 4.3.2 内存屏障
* 硬件层面的内存屏障定义
>
    读屏障：
        让高速缓存中的相应数据失效，在指令之前插入读屏障，可以让高速缓存中的数据失效，强制重新从主存中加载数据。
        禁止重排：同时读屏障会告诉CPU和编译器让先这个屏障前的指令必须先执行。
        如在X86处理器，会强制所有在该指令（ifence）之后的读操作都在这个ifence后面执行，并强制是本地缓存失效，从而重新从主存读取数据
    写屏障：
        写指令后面插入写屏障指令，能让高速缓存中的最新数据更新到主存，使得其他线程可见。
        禁止重排：并且，写屏障会告诉CPU和编译器，后于这个屏障指令的指令必须后执行
    全屏障：
        读写的功能都有
## 4.4 JMM
### 4.4.1 定义如下
1. 所有变量存在主存
2. 每个线程都有自己的工作内存，且对应变量的操作都在工作内存中进行
3. 线程间的访问彼此的变量只能通过主存来共享变量


其中JMM的工作内存和主存都是在寄存器，cpu缓存，物理主存的三个地方


### 4.4.2 JMM规定的八个原子操作

`否关的观点从vvb传播从从从v不错v吧` 
` bnm b b `


| 标题 | 作用对象 | 描述

| Read(读取)| 主存| 从主存到工作内存

| Load(载入)| 主存| 从工作内存到高速缓存

| Use(使用)| 主存| 线程的使用，如运算

| Assign(赋值)| 主存| 线程到工作内存

|store(存储)|主存| 工作内存到主存

| Write(写入)| 主存|  主存之前的

| Lock(锁定)| 主存| 

| Unlock(解锁)| 主存|

JMM的内存屏障
### 4.4.3 

* Load Barrier (读屏障) ：值读之前插入读屏障，让高速缓存中的的数据失效，从主存中读取数据
* Store Barrier (写屏障) :能让写入缓存的数据写回到主存

在实际中的屏障使用：
1. LL:  一个指令实例：load1; loadload; load2; 表示使用LL屏障来保证load1被读取完了后load2才开始读取数据<br>
    在每个volatile的**读操作后**边插入
2. LS： 保证在写之前的完成数据读取操作， load; loadstore;store; <br>
    在每个volatile的**读操作后**边插入
3. SS： store1; ss; store2; 保证store1写入了主存使得对其他可见后，才写入store2      <br>
    在每个volatile的**写操作前**面插入
4. SL: 保证在读之前的完成数据写入操作， store; storeload;load;    <br>
    在每个volatile的**读操作前**边插入

读只有在前面的指令会被禁止排序
写的话前后都会有屏障，分别屏障，前面的屏障ss，禁止前面的普通写与自身重排序；后面的SL屏障，禁止下面的写和自身的重排序

## 4.6 Happen Before 规则
* 
1. 程序顺序执行规则（as if serial）   

2. volatile变量规则：    
    volatile修饰的变量的**写**操作必须**先于**对volatile修饰变量的**读**操作
3. 传递型规则    
    
4. monitor规则    
    加锁，解锁的顺序
5. start规则  
   
6. join规则
## 5. JUC的显式锁
* 显示锁
    
```java
public interface Lock {
//   上锁，无法上锁的话会被阻塞    
    void lock();
//  可以被中断的上锁操作
    void lockInterruptibly() throws InterruptedException;

    /** 来自源码的注释，锁的模板写法
     * Lock lock = ...;
     * if (lock.tryLock()) {
     *   try {
     *     // manipulate protected state
     *   } finally {
     *     lock.unlock();
     *   }
     * } else {
     *   // perform alternative actions
     * }}</pre>
     */
//  尝试获取锁 这个不会阻塞
    boolean tryLock();
//  规定事件内获取锁，同时这个锁也是支持被中断，在获取锁的过程中
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
//  解锁
    void unlock();
//  条件队列，可用于将线程放入等待队列和将线程从等待队列中唤醒
    Condition newCondition();
}

public interface Condition {
    void await() throws InterruptedException;
    void awaitUninterruptibly();
    long awaitNanos(long nanosTimeout) throws InterruptedException;
    boolean awaitUntil(Date deadline) throws InterruptedException;
    void signal();
    void signalAll();

}
```

## 5.1 显式锁介绍

实现了的锁：
```java
public class ReentrantLock implements Lock, java.io.Serializable {
    private final Sync sync;
    abstract static class Sync extends AbstractQueuedSynchronizer {
        abstract void lock();


        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
// 直接获取锁，这个是非公平锁
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            // ...
        }
    }
 // 非公平锁的实现类，他没有重写
    static final class NonfairSync extends Sync {
       final void lock() {
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }
    }
    static final class FairSync extends Sync {
        final void lock() {
            acquire(1);
        }
// 公平锁的实现
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
// 这里会先进行是否有等待队列的判断
                if (!hasQueuedPredecessors() &&
                    compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }

}

public class LockSupport {
    // 唤醒被阻塞的线程
    public static void unpark(Thread thread) {
        if (thread != null)
            UNSAFE.unpark(thread);
    }
    // 阻塞当前线程
    public static void park(Object blocker) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(false, 0L);
        setBlocker(t, null);
    }

    // 阻塞当前线程，停止时间
    public static void parkNanos(Object blocker, long nanos) {
        if (nanos > 0) {
            Thread t = Thread.currentThread();
            setBlocker(t, blocker);
            UNSAFE.park(false, nanos);
            setBlocker(t, null);
        }
    }
    // 阻塞当前线程，一直到那个时间
    public static void parkUntil(Object blocker, long deadline) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(true, deadline);
        setBlocker(t, null);
    }
    // 获取被线程线程的阻塞对象（即锁对象）
    public static Object getBlocker(Thread t) {
        if (t == null)
            throw new NullPointerException();
        return UNSAFE.getObjectVolatile(t, parkBlockerOffset);
    }
    // 无限，阻塞当前线程
    public static void park() {
        UNSAFE.park(false, 0L);
    }
    // 阻塞一定时间
    public static void parkNanos(long nanos) {
        if (nanos > 0)
            UNSAFE.park(false, nanos);
    }
    static final int nextSecondarySeed() {//...
    }
    private static final sun.misc.Unsafe UNSAFE;
    private static final long parkBlockerOffset;
    private static final long SEED;
    private static final long PROBE;
    private static final long SECONDARY;
    static {
        // 初始化语句
    }
}
```

LockSupport的park与Object的wait的比较     
1可以任意地方执行，2只能在代块中调用     
当线程被阻塞的时候，在调用，前者不会报出异常，后者会      
没有阻塞的情况下调用unpark和notify，前者不会抛出异常且是允许的，后者会抛出异常

## 5.2 各种类型锁的比较
* 乐观与悲观     
**CLH自旋锁**(AQS的核心原理)    
CLH的逻辑示意图
![CHL_theory]
也是线程的等待队列，不过他的等待队列的线程还在不断的自旋，判断上一个节点的是否获取倒锁（而不是直接去争取锁资源）

* 公平与非公平    

以ReentantLock为例，他的公平与非公平主要是是否先判断是否有等待队列存在，          
非公平锁不会判断是否存在等待队列直接获取，获取失败了才判断并加入等待队列     
而公平锁需要先判断是否存在等待队列如果存在则直接加入等待队列不去抢占锁

![getLockReentrantLock]

* 可中断与不可中断      
```java
public interface ThreadMXBean extends PlatformManagedObject {
//   检测由于抢占JUC显示锁，java内置锁引起死锁的线程
    public long[] findDeadlockedThreads();
//   检测java内置锁死锁的线程
    public long[] findMonitorDeadlockedThreads();
}
```

* 共享与独享     
排他锁synchronized
共享锁Semaphore,CyclicBarrier（等待多个线程完成后开始执行）
CountDownLatch
```java


public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        Integer size = 5;
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0;i < size; i++){
            Thread thread = new Thread(new Task1(countDownLatch));
            thread.setName("线程----"+i);
            thread.start();
        }
        System.out.println("线程初始化完成,主线线程开始等待");
// 主线程等待，其他线程将CountDownLatch减到0后开始执行
        countDownLatch.wait();
        System.out.println("子线程执行完成了");

    }
//    子线程工作主线程等待的模式

    static class Task1 implements Runnable{
        CountDownLatch latch;

        public Task1(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread()+" 工作一小会");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread()+" 计数减一");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

* 读写锁       

```java
// 读写互斥的锁
public class ReentrantReadWriteLock
        implements ReadWriteLock, java.io.Serializable {
}
```
读写锁只支持写锁降级为读锁，不支持读锁升级为写锁
对与读写锁的改进StampedLock,主要就是将读的操作下（在没有写操作的时候），不给读加上锁，从而提高读的效率
```java
public class StampedLock implements java.io.Serializable {
}
```
# 6. AQS(Abstract Queue Synchronizer)（抽象同步器）
核心就是之前的CHL
CLH的逻辑示意图
![CHL_theory]
不过AQS内部维护的是一个FIFO的双向链表
## 6.1 AQS的核心成员
```java
public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements Serializable {
    private static final long serialVersionUID = 7373984972572414691L;
    private transient volatile AbstractQueuedSynchronizer.Node head;
    private transient volatile AbstractQueuedSynchronizer.Node tail;
    private volatile int state;
    static final long SPIN_FOR_TIMEOUT_THRESHOLD = 1000L;
// VarHandle可以理解为对Unsafe的封装和优化
    private static final VarHandle STATE;
    private static final VarHandle HEAD;
    private static final VarHandle TAIL;

    protected final boolean compareAndSetState(int expect, int update) {
        return STATE.compareAndSet(this, expect, update);
    }
// release的模板方法，
    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }
// 在此方法中没有实现此方法，需要子类实现，以作为回调函数
    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }
//  唤醒后继线程
    private void unparkSuccessor(Node node) {
        /*
         * If status is negative (i.e., possibly needing signal) try
         * to clear in anticipation of signalling.  It is OK if this
         * fails or if status is changed by waiting thread.
         */
        int ws = node.waitStatus;
        if (ws < 0)
            node.compareAndSetWaitStatus(ws, 0);

        /*
         * Thread to unpark is held in successor, which is normally
         * just the next node.  But if cancelled or apparently null,
         * traverse backwards from tail to find the actual
         * non-cancelled successor.
         */
//  唤醒后继线程
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node p = tail; p != node && p != null; p = p.prev)
                if (p.waitStatus <= 0)
                    s = p;
        }
        if (s != null)
            LockSupport.unpark(s.thread);
    }



    static final class Node {
        static final AbstractQueuedSynchronizer.Node SHARED = new AbstractQueuedSynchronizer.Node();
        volatile int waitStatus;
        // 双向链表的标志
        volatile AbstractQueuedSynchronizer.Node prev;
        volatile AbstractQueuedSynchronizer.Node next;
        volatile Thread thread;
        AbstractQueuedSynchronizer.Node nextWaiter;
        private static final VarHandle NEXT;
        private static final VarHandle PREV;
        private static final VarHandle THREAD;
        private static final VarHandle WAITSTATUS;
    }
}
```
* 6.2 模板模式
* 6.3 AQS锁的抢占原理
* 6.4 AQS的释放锁
* 6.5 节点的出入队
* 6.6 ReentrantLock的枪锁流程(里面有一个，公平和非公平枪锁)

## 6.7 AQS条件队列
* Condition原理
Condition的条件队列是单链表（保存了队尾和对头），他的出队入队都是在和AQS打交道。      
条件队列：入队（wait），是将AQS的对头移到条件队列的队尾     
        出队（single），是将条件队列的对头放置AQS的队尾

# 7. JUC容器

## 7.1 CopyOnWriteArrayList
这里有一个COW原则来优化读写速度      
写时赋值的原则为，写的时候是对写的数据拷贝一份再对拷贝的数据（一块内存，这里是内存块还是？？？）进行操作，写完后将数据指向拷贝的数据完成写的操作        
这里还有一个跳表问题     
> 
          
     Head nodes          Index nodes      
     +-+    right        +-+                      +-+     
     |2|---------------->| |--------------------->| |->null       
     +-+                 +-+                      +-+         
      | down              |                        |      
      v                   v                        v      
     +-+            +-+  +-+       +-+            +-+       +-+       
     |1|----------->| |->| |------>| |----------->| |------>| |->null     
     +-+            +-+  +-+       +-+            +-+       +-+       
      v              |    |         |              |         |        
     Nodes  next     v    v         v              v         v        
     +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+       
     | |->|A|->|B|->|C|->|D|->|E|->|F|->|G|->|H|->|I|->|J|->|K|->null     
     +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+  +-+       
    


## 7.2 BlockingQueue 
就是在添加和删除的的时候添加了阻塞操作。        
方法：         
    // 添加方法     
    boolean add(E e);  // 添加成功返回true，失败返回IllegalException       
    boolean offer(E e);   // 失败或者队满，返回false     
    void put(E e) throws InterruptedException;  //  如果队满则阻塞
    boolean offer(E e, long timeout, TimeUnit unit) // 带时间的     
        throws InterruptedException;        
    E take() throws InterruptedException; // 没有元素则阻塞        
    E poll(long timeout, TimeUnit unit) // 移除队头，失败返回空
        throws InterruptedException;        
    boolean remove(Object o); // 失败返回 false
    boolean contains(Object o); // 查找
    int drainTo(Collection<? super E> c);
    int drainTo(Collection<? super E> c, int maxElements);      
* 子类        
ArrayBlockingQueue      
DelayQueue      
LinkedBlockingQueue     
PriorityBlockingQueue       
SynchronousQueue  注意点       

## 7.3 ConcurrentHashMap
分1.7和1.8
*   1.7   
    1.7 之前采用了分段锁的机制，每次初始化的时候（他的初始化最小长度位16，即使你指定跟小的数字）       
    一个ConcurrentHashMap存储存储了16个Segment
    而每个Segement里面存储的才是真正的元素。每次而锁的单位为Segment
*   1.8 之后
    ConcurrentHashMap, 他是锁定的是，hashh表中，数组的元素的入口，
```java
public class ConcurrentHashMap{

/** Implementation for put and putIfAbsent */
    final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (key == null || value == null) throw new NullPointerException();
        int hash = spread(key.hashCode());
        int binCount = 0;
        for (Node<K,V>[] tab = table;;) {
            Node<K,V> f; int n, i, fh; K fk; V fv;
            if (tab == null || (n = tab.length) == 0)
                tab = initTable();
//  f = tabAt(tab, i = (n - 1) & hash) 上锁的对象==========================================================
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
//  通过casObject计算出table[i]
                if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value)))
                    break;                   // no lock when adding to empty bin
            }
            // ...
            else {
                V oldVal = null;
// 真正上锁的位置=============================================================================
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        if (fh >= 0) {
                            binCount = 1;
                            for (Node<K,V> e = f;; ++binCount) {
                                K ek;
                                if (e.hash == hash &&
                                    ((ek = e.key) == key ||
                                     (ek != null && key.equals(ek)))) {
                                    oldVal = e.val;
                                    if (!onlyIfAbsent)
                                        e.val = value;
                                    break;
                                }
                                Node<K,V> pred = e;
                                if ((e = e.next) == null) {
                                    pred.next = new Node<K,V>(hash, key, value);
                                    break;
                                }
                            }
                        }
                        else if (f instanceof TreeBin) {
                            Node<K,V> p;
                            binCount = 2;
                            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                           value)) != null) {
                                oldVal = p.val;
                                if (!onlyIfAbsent)
                                    p.val = value;
                            }
                        }
                        else if (f instanceof ReservationNode)
                            throw new IllegalStateException("Recursive update");
                    }
                }
                if (binCount != 0) {
                    if (binCount >= TREEIFY_THRESHOLD)
                        treeifyBin(tab, i);
                    if (oldVal != null)
                        return oldVal;
                    break;
                }
            }
        }
        addCount(1L, binCount);
        return null;
    }
    static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
        return (Node<K,V>)U.getObjectAcquire(tab, ((long)i << ASHIFT) + ABASE);
    }

    static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i,
                                        Node<K,V> c, Node<K,V> v) {
        return U.compareAndSetObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
    }

    static final <K,V> void setTabAt(Node<K,V>[] tab, int i, Node<K,V> v) {
        U.putObjectRelease(tab, ((long)i << ASHIFT) + ABASE, v);
    }
}
```
# 8. 高并发设计模式

## 8.1 单例
```java
// 使用静态内部类实现单例模式
public class SingleTon {

    static class LazyHolder{
        private static final SingleTon INSTANCE = new SingleTon();
    }
    private SingleTon(){

    }
    public static SingleTon getInstance(){
        return LazyHolder.INSTANCE;
    }
}

```
## 8.2 Master-Worker(类似于主从)
以一个主服务线程，负责委派线程执行具体的任务     
而自己执行的调度以及最后的线程结束       

## 8.3 ForkJoin(分治原则)

## 8.4 生产者消费者模式

## 8.5 Future

## 8.6 异步回调的衍生

回调的最底层原子或者思想或者实现方法          
Guava的异步回调          
Netty的异步回调      
CompletableFuture(jdk1.8支持的原生异步回调)      
