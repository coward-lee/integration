package book.chapter01.thread_pool;

import lombok.Data;
import org.junit.Test;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadLocal的实验
 */
public class TestThreadLocal {

    @Data
    static class Foo{
        static final AtomicInteger ATOMIC = new AtomicInteger(0);
        int index =  0;
        int bar = 10;
        public Foo(){
            index = ATOMIC.incrementAndGet();

        }

        @Override
        public String toString() {
            return "Foo{" +
                    "index=" + index +
                    ", bar=" + bar +
                    '}';
        }

        public void setBar(int bar) {
            this.bar = bar;
        }
        public static Foo getFoo(){
            return new Foo();
        }
    }
    private static final ThreadLocal<Foo> LOCAL = new ThreadLocal<>();

    public static void main(String[] args) {
        ThreadPoolExecutor executor = ThreadUtil.getMixedTargetThreadPool();
        for (int i = 0; i < 5; i++){
            executor.execute(()->{
                if (LOCAL.get() == null){
                    LOCAL.set(new Foo());
                }
                System.out.println("初始化本地值："+LOCAL.get());
                for (int j = 0; j < 10; j++){
                    Foo foo = LOCAL.get();
                    foo.setBar(foo.bar+1);  // 增值一
                    sleep(10);
                }
                System.out.println("本地累加十次后："+LOCAL.get());
                LOCAL.remove();
            });
        }
    }

    @Test
    public void testInitThreadLocal(){
//        Foo::getFoo静态方法的调用
        ThreadLocal<Foo> fooThreadLocal = ThreadLocal.withInitial(Foo::getFoo);
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

}
