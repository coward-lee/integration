package org.lee.netty.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public class SubSelector implements Runnable{
    final Selector selector;
    public SubSelector(Selector selector) {
        this.selector = selector;
    }


    @Override
    public void run() {
        try{
            while (!Thread.interrupted()){
                selector.select();
                Set<SelectionKey> set = selector.selectedKeys();
                set.forEach(key->{
                    dispatch(key);
                });
                set.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void dispatch(SelectionKey sk){
        Runnable handler = (Runnable) sk.attachment();
        if (handler != null){
            handler.run();
        }
    }

}
