package book.chapter05;

import java.util.concurrent.CountDownLatch;

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
        countDownLatch.wait();
        System.out.println("子线程执行完成了");

    }

    // 主线程来满足子线程的条件
    public static void case1(String[] args) throws InterruptedException {
        Integer size = 5;
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0;i < size; i++){
            Thread thread = new Thread(new Task(countDownLatch));
            thread.setName("线程----"+i);
            thread.start();
        }
        System.out.println("线程初始化完成");
        Thread.sleep(1000);

        System.out.println("开始减计数");

        for (int i = 0;i < size; i++){
            System.out.println("放了：" + i + "个");
            countDownLatch.countDown();
        }
    }
//    子线程等待的模式
    static class Task implements Runnable{
        CountDownLatch latch;

        public Task(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread()+" 工作一小会");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread()+" 等待前");
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
