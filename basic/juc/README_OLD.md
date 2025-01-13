
# 学习记录=== juc的一些相关学习


# 基础记录
## 创建线程的四种方式
> 
    不管怎么样子的创建方式最终都是实现的Runnable接口
### 方式一：实现Runnable

### 方式二：继承Thread

### 方式三：使用Callable和FutureTask
>
    //实现一个callable接口
    class ReturnTask implements Callable<V> {
        public V call() throws Exception {xxxx}
    }
    // 调用以及执行
    ReturnTask returnTask = new ReturnTask();
    FutureTask<Long> futureTask = new FutureTask<V>(returnTask);
    Thread thread = new Thread(futureTask, "returnThread");
    thread.start();
### 方式四：线程池创建线程
>
   	private static ExecutorService pool = Executors.newFixedThreadPool(3);
    DemoThread implements Runnable{
 		public void run() {xxxx}
	}
	class ReturnTask implements Callable<Long> {
    	public Long call() throws Exception {xxx}
	}
	// 执行线程实现runnable接口的线程
	pool.execute(new DemoThread());
    // 执行线程实现Callable接口的线程，会返回一个Future实例
	Future f = pool.submit(new ReturnTask());
	Object result = f.get();
	
# 线程过期的函数
suspend,stop都容易造成线程锁定的对象无法释放，


# java中的线程状态

### new

### runnable

#### 运行，阻塞，可运行

### terminated

### BLOCKED
* 该线程会在owner线程释放锁时唤醒
### WAITING
* thread.sleep()方法实现
* owner 线程发现条件不满足，调用wait方法，即可进入waitSet状态变为waiting
* 在执行了notify或者notifyAll之后该状态的会进入就绪态（EntryList开始竞争）
### TIMED_WAITING
thread.join()方法实现

>   
    // 线程状态枚举：
    public enum State {
        /**
         * Thread state for a thread which has not yet started.
         * 新建
         */
        NEW,

        /**
         * Thread state for a runnable thread.  A thread in the runnable
         * state is executing in the Java virtual machine but it may
         * be waiting for other resources from the operating system
         * such as processor.
         * 可执行状态，可以执行了不过还在等待操作系统的资源（比如：CPU资源，当然可能还有其他的）
         */
        RUNNABLE,

        /**
         * Thread state for a thread blocked waiting for a monitor lock.
         * A thread in the blocked state is waiting for a monitor lock
         * to enter a synchronized block/method or
         * reenter a synchronized block/method after calling
         * {@link Object#wait() Object.wait}.
         * 等待monitor lock（synchronized的锁对象/类）时就是blocked状态，
         * 或者锁对象执行了wait()方法后，正在执行的线程也会进入blocked状态
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
         * For example, a thread that has called <tt>Object.wait()</tt>
         * on an object is waiting for another thread to call
         * <tt>Object.notify()</tt> or <tt>Object.notifyAll()</tt> on
         * that object. A thread that has called <tt>Thread.join()</tt>
         * is waiting for a specified thread to terminate.
         * 原因一：执行了如下的方法： Object.wait、 Thread.join、 LockSupport#park()
         * 原因二：线程同步时使用wait和notify/notifyAll时使用了wait方法后的状态。
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
         * 调用如下方法：Thread.sleep、 Object.wait、 Thread.join、LockSupport#park、NanosLockSupport#parkUntil
         * 这些方法都是带了时间参数的所以为TIMED可以理解为定时了的（或者规定了时间的）
         */
        TIMED_WAITING,

        /**
         * Thread state for a terminated thread.
         * The thread has completed execution.
         * 线程结束运行了
         */
        TERMINATED;
    }



# 锁

## 基础知识
    Java对象结构
    堆区：
        对象头
            Mark Word 
            Class Pointer
            Array Length
        成员变量：
            Feild 1
            Feild 2
            Feild 3
            ..（这里的成员是非静态成员，即对象的成员不是类的成员，静态成员变量是类的成员变量）
            字节对齐
    方法区 Class Pointer -> 类的Class对象
        

## "线程八锁"
就是synchronized锁定的不同对象的情况<br>
* 锁对象：
>
	this表示锁定当前对象（代码块） monitorentre和monitorexit
	other锁定其他的实例（代码块）monitorentre和monitorexit
	直接用在非静态方法上面（锁定this对象） ACC_SYNCHRONIZED
	直接用在静态方法上面（锁定整个类） 
## 常见的线程安全类
* String
* Integer
* StringBuffer
* Random
* Vector List的线程安全实现
* Hashtable
* juc

## mark word
* object header
>
    markword(64/23bit)
    class pointer
    array length(可选) 如果为数组
    fields
    ..（这里的成员是非静态成员，即对象的成员不是类的成员，静态成员变量是类的成员变量）
## monitor →监视器或者管程
>
    
    astore_1     ;lock引用-> slot 1
    5:  monitorenter
    6:  xxxx
        xxxx
        xxxx        
    19: astore_2   ;出现异常后使用的内容(再存一次引用)
    aload_1   ;<- slot引用
    monitorexit
    exception table:
    from to target type
    6    16  19     any
    19   22  19     any
## synchronized
>
    老是忽略释放点
### 四个阶段

* 获取并写入自己的线程ID lock cmpxchg addr1 addr2

#### 无锁态
* 没有线程锁住对象
#### 偏向锁
* 自己锁住同时自己又去拿自己锁住的内容
* 将自己的线程ID写道对象的markword(前面的54位)之上，并把标志位置为：101（偏向锁标志）
* 批量操作
* 问题这里有一个获取MarkWord的过程并写入自己的线程ID的过程（或者过程的解决方案，就是利用CAS嘛但是是否是涉及到指令级别的处理）
* 偏向锁的撤销：
    1. 多个线程同时竞争偏向锁，（这个时候会被升级为轻量级锁）(这里还有一个安全点检查的问题)
    2. 当调用hashCode();System.identifyHashCode();的时候见hasCode放入markword中
>
    批量重偏向：当撤销偏向锁并给另一个线程的对象锁阈值超过20次后，jvm会再给这些对象重新置为偏向锁给新的线程,意思就是前20次是轻量级锁
    后面就开始为偏向锁了     即轻量级->偏向锁
    批量撤销：当撤销偏向锁阈值超过40次之后，整个类的对象都不能执行偏向锁
    

#### 轻量级锁
* 在有两个线程竞争一个对象的情况下就会从原来的偏向锁升级到轻量级锁
* 前面的62位为锁记录指针后面两位00
* lock cmpxchg（可以使用版本号的形式来防止ABA现象）
* 已经有现成拿了锁，新的一个线程过来了就把偏向锁升级到轻量级锁
* 同时在jdk1.6之后使用了自适应自旋，大概意思是以前拿到过就自旋久一点，没有拿到过就自旋时间短一点。

#### 重量级锁
* markword：62位的monitor对象指针 10
* 同步操作交给了我操作系统实现，这里的等待线程就会进入等待队列不会再消耗CPU了（在等待时候不会消耗）
* _WaitSet、cxq、_EntryList，这三个队列存放抢夺重量级锁的线程，及synchronized进入重量级之后线程会被放置在这三个队列之中
* cxq请求锁的线程首先会被放在这个队列中，这里有个ABA问题是，因为他的竞争模式还是利用的CAS,他是在对头插入，队尾取出（这样来避免ABA问题，没有懂todo）：详细在JAva高并发核心编程 卷2 164页
* EntryList Cxq中那些符合资格的线程会被移动到EntryList(什么是符合资格的怎么计算或者统计的)  todo 详细在JAva高并发核心编程 卷2 164页
* WaitSet 在ObjectMonitor中调用了wait的之后的会被放入到这个里面 详细在JAva高并发核心编程 卷2 165页
* 以下的一些队列可以证明：在调用notify/notifyAll的时候具体的操作是WaiSet中的第一个/全部线程被放到EntryList队列中
>
    // monitor结构体 cpp实现
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

[synchronized-theory]:img/synchronized_basic.jpg
synchronized关键字的四个阶段对
**mark word**
的状态变化:
![synchronized-theory]

## 锁消除
    java -XX:-EmliminiateLocks xxx
    
# 三个API：wait、notify、notifyAll
## wait
让进入object监视器的线程到waitSet等待
wait和sleep进入的都是timed_waiting
## notify
正在waitSet等待的线程中唤醒一个
## notifyAll
全部唤醒

## 虚假唤醒问题
 * 将原来的sleep改为wait和notify的情况
 * 多个线程等待的情况
 * 使用notify唤醒换成可能造成虚假唤醒情况
 * 所以不该采用notify而是采用notifyAll();
 * notifyAll + 循环判断是否唤醒   
 * 内容见：WaitAndNotifyDemo04.java
 * 在单个/所有线程被唤醒的时候之所有需要使用循环的判断，就是因为在线程被唤醒后线程进入争抢锁状态可能再次拿不到锁，或者被notify唤醒后其实此时不满足他的运行条件，所以通过循环再次进入waitSet（等待状态）

# 多线程的设计模式
##  保护性暂停模式
      应用在两个线程之间的同步问题

>   
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
    
>     	
    // 通知唤醒
    public void complete(Object request){
    		synchronized (this){
    			this.respone = request;
    			this.notifyAll();
    		}
    	}


# 线程池
    单线程、固定数量线程、缓存的线程（无限数量的工作线程）、可调度线程
## 线程池创建的注意事项
    在进行线程池创建的时候不能直接使用Exectors直接进行四种类型的线程池创建。
    而是使用ExecutorThreadPool来创建对象
        创建方法如下：        
            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                       1, corePoolSize 核心数数量， 这个可以后期动态修改
                       100, maximumPoolSize 线程数的上限， 这个可以后期动态修改
                       100,  // keepAliveTime 空闲存活时长， 这个可以后期动态修改
                       TimeUnit.MILLISECONDS, // 时间单位  
                       new LinkedBlockingDeque<>(100),  // workQueue 任务排队的队列
                       // threadFacotry 线程工厂
                       // handler 拒绝策略
               );
## 像线程池提交线程的两种方式
    方式一：
        pool.execute(Runnable run)
    方式二：
        <T> Future<T> submit(Callable<T> task);
        <T> Future<T> submit(Runnable task, T result);
        Future<?> submit(Runnable task);
     Tip:不管submit怎样他都是最终调用的是execute

## 线程工厂
        static public class SimpleThreadFactory implements ThreadFactory{
            static AtomicInteger threadNo = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                String threadName = "simpleThread-"+threadNo.getAndDecrement();
                System.out.println("创建了一个线程，名："+threadName);
                Thread thread = new Thread(r,threadName);
                return thread;
            }
        }
## 线程阻塞队列
    ArrayBlockingQueue
    
## 调度器回调方法
    new ThreadPoolExecutor(
        protected void terminated() {// 线程池的中止
        protected void beforeExecute(Thread t, Runnable r) {// 执行任务之前的回调
        protected void afterExecute(Runnable r, Throwable t) {// 执行任务之后的回调
     }
        
 ## 线程拒绝策略
    AbortPolicy  : 会抛出异常
    DiscardPlicy ： 不会抛出异常
    DiscardOldestPlicy : 抛弃最老的那个
    CallerRunsPolicy： 如果添加失败提交任务的线程会去执行该任务，不会使用线程池来执行此任务，
    自定义策略：实现RejectedExecutionHandler
 超过线程池的核心数+线程等待队列数，这样就会被拒绝
## 线程池状态：
        RUNNING    = -1 << COUNT_BITS;
            创建线程的状态
        SHUTDOWN   =  0 << COUNT_BITS;
            关闭，线程池不在接受新的任务
        STOP       =  1 << COUNT_BITS;
            不接受新的任务，这个会中断所有线程
        TIDYING    =  2 << COUNT_BITS;
            当所有线程都执行完了，会调用：terminated的回调函数
        TERMINATED =  3 << COUNT_BITS;
            调用这个：terminated的回调函数之后的状态
        
## ThreadLocal
    
    现在一个线程实例还有一个ThreadLocal实例
            而Thread Local的容器是：ThreadLocalMap的Map是Entry（是他自己的内部类）
            他的冲突解决方式是线性探测，会循环探测因为他有扩充因子不会填满
            rehash是扩容函数
    ThreadLcoal的初始化
        ThreadLocal<Foo> fooThreadLocal = ThreadLocal.withInitial(Foo::getFoo);
        带默认值初始化
    每个线程内部有个Map（即：ThreadLocalMap），他的Key：ThreadLocal，Value：Object
## Atomic（核心就是CAS）
      核心为Unsafe下面的各种compareAndSetXXX(old,excepted)
      todo CAS又是通过lock cmpxchg xxx来实现的
      todo     @HotSpotIntrinsicCandidate  在各种方法上面的注释什么意思
      原子类
      注意点：
        主要就是通过cas（lock cmpxchg指令实现）
      问题点：在处理ABA问题的时候AtomicStampRefence他是直接交换的reference
        核心是利用了AtomicReference基础的原子类（这的操作的原理）todo
### 原子类
*    基本类型：AtomicInteger/Long/Boolean
*    数组：AtomicInteger/Long/ReferenceArray
*    类：Atomic(Markable/Stamped)Reference
*    字段AtomicInteger/Long/ReferenceFieldUpdater
*    原子类的ABA问题的解决
>   
    主要就是利用AtomicStamped/MarkableReference来解决
    Stamped再通过构建一个Pair来对或者Pair进行CAS实现原子性
     private static class Pair<T> {
            final T reference;
            final int stamp;
            static <T> Pair<T> of(T reference, int stamp) {
                return new Pair<T>(reference, stamp);
            }
        }
    public boolean compareAndSet(V   expectedReference,
                                 V   newReference,
                                 int expectedStamp,
                                 int newStamp) {
        Pair<V> current = pair;
        return
            expectedReference == current.reference &&
            expectedStamp == current.stamp &&
            ((newReference == current.reference &&
              newStamp == current.stamp) ||
             casPair(current, Pair.of(newReference, newStamp)));
    }
    private boolean casPair(Pair<V> cmp, Pair<V> val) {
        return PAIR.compareAndSet(this, cmp, val);
    }
    @MethodHandle.PolymorphicSignature
    @HotSpotIntrinsicCandidate
    boolean compareAndSet(Object... args);
### LongAddr 
[ Base ][cell 1][cell 2]
>
    他是继承于Striped64：
        /**
         * Table of cells. When non-null, size is a power of 2.
         * 拆分后的数据
         */
        transient volatile Cell[] cells;
    
        /**
         * Base value, used mainly when there is no contention, but also as
         * a fallback during table initialization races. Updated via CAS.
         * 基础
         */
        transient volatile long base;
    
        /**
         * Spinlock (locked via CAS) used when resizing and/or creating Cells.
         * 锁整个数组
         */
        transient volatile int cellsBusy;

            
## 有序与可见性
    为什么会指令重排：
        因为局部性原理，
### MESI
    目的：解决内存可见性的问题的手段 ，可见性是指CPU内核A在自己的缓存中更改了数据DATA_A后CPU内核B也能看见数据被修改了
    实现方式：1.锁总线，2.锁缓存行
     缓存行的操作，
###   JMM（JAVA Memory Model）
* 有序性，编译器重排序，CPU重排序
* 硬件屏障
>
    读屏障: 在指令前面插入读屏障，可以让告诉缓存中的数据失效，强制从主存加载数据
    写屏障：指令后面插入写屏障，将数据从高速缓存写入到内存
    全屏障：读写的功能都来
* JMM的8个操作
> 
    JVM在是实现这8个操作的时候必须是原子性的，
    即:以下八个操作是原子操作
    1.读取read 主存->工作内存
    2.载入load 工作内存->cache
    3.使用use  cache->线程
    4.赋值assign 线程->工作内存
    5.存储store  工作内存->主存
    6.写入write  主存->主存
    7.lock      主存->主存
    8.unlock    主存->主存
    
* JMM 内存屏障
>        Load Barrier(读屏障)   在读指令前插入读屏障，可让高速缓存失效，重新从主存加载内存
>        Store Barrier(写屏障)  在写指令之后插入写屏障，能让写入缓存的最新数据写回内存
>        LL
>        SS
>        LS
>        SL
###    volatile 
    不具有原子性

