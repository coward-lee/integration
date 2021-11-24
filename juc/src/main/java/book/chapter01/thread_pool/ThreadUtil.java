package book.chapter01.thread_pool;


import java.util.concurrent.*;

public class ThreadUtil {

    private final static int MIXED_MAX = 127;
    private final static String MIXED_THREAD_AMOUNT = "mixed.thread.amount";
    private final static int KEEP_ALIVE_SECONDS = 30;
    private final static String KEEP_ALIVE_SECONDS_PROPERTY = "mixed.thread.amount";




    private ThreadUtil(){

    }

    private static class MixedTargetThreadPoolLazyHolder{
        private final static int max = (null != System.getProperties().get(MIXED_THREAD_AMOUNT)) ? Integer.parseInt(System.getProperty(MIXED_THREAD_AMOUNT)) : MIXED_MAX;
        private final static int keepAliveSeconds = (null != System.getProperties().get(KEEP_ALIVE_SECONDS_PROPERTY)) ? Integer.parseInt(System.getProperty(KEEP_ALIVE_SECONDS_PROPERTY)) : KEEP_ALIVE_SECONDS;
        private final static ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                max,
                max,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new CreateThreadPoolDemo.SimpleThreadFactory(),
                new CreateThreadPoolDemo.CustomIgnorePolicy()
        );
        static{
//            允许核心线程为超时被淘汰,JVM的线程池关闭的回调函数
            EXECUTOR.allowCoreThreadTimeOut(true);
            Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("关闭线程池的线程", new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    shutdownThreadPoolGracefully(EXECUTOR);
                    return null;
                }
            }));
        }
    }
    static class ShutdownHookThread extends Thread{
        private final Callable<?> callable;

        public ShutdownHookThread(String name, Callable<?> callable) {
            super(name);
            this.callable = callable;
        }

        @Override
        public void run() {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 优雅的关闭线程
    public static void shutdownThreadPoolGracefully(ExecutorService pool){
        if (pool.isShutdown()){
            return;
        }
        try{
            pool.shutdown();
        }catch (SecurityException e){
            return;
        }catch (NullPointerException e){
            return;
        }
        try{
            if (!pool.awaitTermination(60,TimeUnit.SECONDS)){
                pool.shutdownNow();
                if (!pool.awaitTermination(60,TimeUnit.SECONDS)){
                    System.err.println("线程池任务未正常结束");
                }
            }

        } catch (InterruptedException e) {
            pool.shutdownNow();
            return;
        }

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

    public static ThreadPoolExecutor getMixedTargetThreadPool(){
        return new MixedTargetThreadPoolLazyHolder().EXECUTOR;
    }
}
