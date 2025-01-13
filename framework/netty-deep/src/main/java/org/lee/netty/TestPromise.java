package org.lee.netty;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.ExecutionException;

public class TestPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        EventLoop loop = new NioEventLoopGroup().next();
        DefaultPromise<Integer> defaultPromise = new DefaultPromise<>(loop);
        new Thread(()->{
            System.out.println("开始计算");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            defaultPromise.setSuccess(80);
        }).start();
        System.out.println("d等待结果是："+ defaultPromise.get());
    }
}
