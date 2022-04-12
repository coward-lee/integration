package org.lee.study.reactor;

import java.util.List;

public class MyEventProcessor {
    public static <T> MyEventListener<T> register(MyEventListener<T> listener){
        System.out.println("注册了："+listener);
        return listener;
    };
}
interface MyEventListener<T> {
    void onDataChunk(List<T> chunk);
    void processComplete();
    void onError(Throwable e);
}
