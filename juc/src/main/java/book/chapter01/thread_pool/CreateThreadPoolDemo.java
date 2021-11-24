package book.chapter01.thread_pool;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CreateThreadPoolDemo {
    public static final int SLEEP_GAP = 500;

    static class TargetTask implements Runnable{
        public String name;
        static AtomicInteger integer = new AtomicInteger();

        public TargetTask() {
            this.name = "任务-"+integer.get();
            integer.incrementAndGet();
        }

        @Override
        public void run() {
            try {
                System.out.println("task:"+name+"开始");
                Thread.sleep(SLEEP_GAP);
                System.out.println("task:"+name+"结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void testSingle(){

        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5;i++){
            pool.execute(new TargetTask());
            pool.execute(new TargetTask());
        }
        pool.shutdown();
    }
    public static void testFixed(){
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5;i++){
            pool.execute(new TargetTask());
            pool.execute(new TargetTask());
        }
        pool.shutdown();
    }
    public static void testCached(){
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 5;i++){
            pool.execute(new TargetTask());
            pool.execute(new TargetTask());
        }
        pool.shutdown();
    }
    public static void testScheduled(){
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        for (int i = 0; i < 2;i++){
            pool.scheduleAtFixedRate(new TargetTask(),0,500, TimeUnit.MILLISECONDS);
//            pool.execute(new TargetTask());
//            pool.execute(new TargetTask());
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }


    public static void main(String[] args) {
        testScheduled();;
    }


    /**
     * 测试callable返回值
     */

    @Test
    public void testSubmit01() throws InterruptedException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        Future<Integer> future = pool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                return 100;
            }
        });
        try{
            System.out.println("开始请求结果");
            Integer ret = future.get();
            System.out.println(ret);
            System.out.println("结束请求结果");
        }catch (InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
//        Thread.sleep(10000);
        pool.shutdown();
    }
    /**
     * 测试异常返回
     */
    static class TargetTaskWithError extends  TargetTask{
        @Override
        public void run() {
            super.run();
            throw  new RuntimeException("异常来了来自："+ name);
        }
    }
    @Test
    public void testSubmit02() throws InterruptedException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        pool.execute(new TargetTaskWithError());
        Future future = pool.submit(new TargetTaskWithError());
        try{
            if (future.get() == null){
                System.out.println("任务完成");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        pool.shutdown();
    }

    @Test
    public void testThreadPoolExecutor(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                100,
                100,  // 空闲存活时长
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(100)
        );
        for (int i = 0; i < 5;i++){
            final  int taskIndex = 1;
            executor.execute(() ->{
                System.out.println("taskIndex:"+taskIndex);
                sleep(Long.MAX_VALUE);
            });
        }
        while(true){
            System.out.println("- activeCount:"+executor.getActiveCount()+
                    " - taskCount"+executor.getTaskCount());
            System.out.println("wait_queue:"+executor.getQueue().size());
            sleep(1000);
        }

    }

    private static void sleep(int mill){
        try {
            Thread.sleep(mill);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void sleep(Long mill){
        try {
            Thread.sleep(mill);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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

    /**
     * 线程工厂
     */
    @Test
    public void testThreadFactory(){
        ExecutorService pool = Executors.newFixedThreadPool(2, new SimpleThreadFactory());
        for ( int i = 0;i < 5;i++){
            pool.submit(new TargetTask());
        }
        sleep(10000);
        System.out.println("关闭线程池");
        pool.shutdown();
    }

    @Test
    public void testHooks(){
        final Long[] startTIme = {0L};
        ExecutorService pool = new ThreadPoolExecutor(
                2, 4, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>()){
            @Override
            protected void terminated() {
                System.out.println("调度器中止");
            }

            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                System.out.println("异步任务，线程执行之前的方法");
                startTIme[0] = System.currentTimeMillis();
                super.beforeExecute(t,r);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r,t);
                System.out.println(System.currentTimeMillis()-startTIme[0]);
                System.out.println("线程池关闭之后");
            }
        };

        for (int i = 1; i < 5; i++){
            pool.execute(new TargetTask());
        }
        sleep(10);
        System.out.println("关闭线程：");
        sleep(10000);
        pool.shutdown();;
    }


    public static class CustomIgnorePolicy implements RejectedExecutionHandler{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println(r+" rejected"+" - "+executor.getTaskCount());
        }
    }
    @Test
    public void testCustomPolicy(){
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2,4,10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),
                new SimpleThreadFactory(),
                new CustomIgnorePolicy()
        );
//        预先启动核心线程
        pool.prestartAllCoreThreads();
        for (int i = 1;i<=10;i++){
            pool.execute(new TargetTask());
        }
        sleep(10000);
        System.out.println("线程关闭");;
        pool.shutdown();
    }


}
