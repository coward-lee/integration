package org.lee.study.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.CountDownLatch;

public class ConcurrentSplitCalculateSum {

    private static class ForkTask extends Thread {

        private int result = 0;

        private final Spliterator<Integer> spliterator;
        private final CountDownLatch latch;

        public ForkTask(Spliterator<Integer> spliterator,
                        CountDownLatch latch) {
            this.spliterator = spliterator;
            this.latch = latch;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            spliterator.forEachRemaining(num -> result = result + num);
            long end = System.currentTimeMillis();
            System.out.printf("线程[%s]完成计算任务,当前段计算结果:%d,耗时:%d ms\n",
                    Thread.currentThread().getName(), result, end - start);
            latch.countDown();
        }

        public int result() {
            return result;
        }
    }


    private static int join(List<ForkTask> tasks) {
        int result = 0;
        for (ForkTask task : tasks) {
            result = result + task.result();
        }
        return result;
    }

    private static final int THREAD_NUM = 4;

    public static void main(String[] args) throws Exception {
        List<Integer> source = new ArrayList<>();
        for (int i = 1; i < 101; i++) {
            source.add(i);
        }
        // 每次都是除以2的分片
        Spliterator<Integer> root = source.stream().spliterator();
        List<Spliterator<Integer>> spliteratorList = new ArrayList<>();
        Spliterator<Integer> x = root.trySplit();
        Spliterator<Integer> y = x.trySplit();
        Spliterator<Integer> z = root.trySplit();
        spliteratorList.add(root);
        spliteratorList.add(x);
        spliteratorList.add(y);
        spliteratorList.add(z);
        List<ForkTask> tasks = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(THREAD_NUM);
        for (int i = 0; i < THREAD_NUM; i++) {
            ForkTask task = new ForkTask(spliteratorList.get(i), latch);
            task.setName("fork-task-" + (i + 1));
            tasks.add(task);
        }
        tasks.forEach(Thread::start);
        latch.await();
        int result = join(tasks);
        System.out.println("最终计算结果为:" + result);


    }

    public <T> void test(){
        T[] array;
    }
}
