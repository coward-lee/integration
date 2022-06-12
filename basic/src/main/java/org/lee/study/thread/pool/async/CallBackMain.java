package org.lee.study.thread.pool.async;

import java.time.LocalDateTime;

public class CallBackMain {
    public static void main(String[] args) {
        FutureTask<String> stringFutureTask = new FutureTask<>(()-> {
            System.out.println("开始产生数据:"+ LocalDateTime.now());
            Thread.sleep(3000);
            System.out.println("结束"+ LocalDateTime.now());
            return "xxxxx";
        }){
            @Override
            public void onSuccess(String data) {
                System.out.println("自己定义的  onSuccess:"+data+" : "+ LocalDateTime.now());
            }
        };
        Thread thread = new Thread(stringFutureTask);
        stringFutureTask.asyncApply(data-> System.out.println("自己定义的 asyncApply:"+data));
        thread.start();
//        stringFutureTask.
    }
}
