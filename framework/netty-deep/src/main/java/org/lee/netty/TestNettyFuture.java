package org.lee.netty;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class TestNettyFuture {
    static Logger log = LoggerFactory.getLogger(TestJdkFuture.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        EventLoop loop = loopGroup.next();
        Future<Integer> future = loop.submit(() -> {

            log.info("开始执行");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });
        log.info("主线程基本信息");

//        log.info("结果是：{}", future.get());
        future.addListener(future1 -> log.info("结果是：{}", future1.getNow()));
    }
}
