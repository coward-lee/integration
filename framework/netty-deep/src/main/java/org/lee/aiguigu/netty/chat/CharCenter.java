package org.lee.aiguigu.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class CharCenter {
    private static final Logger log = LoggerFactory.getLogger(CharCenter.class);
    public static final Integer PORT = 9090;
    private static final DefaultChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//    private static final List<Channel> clients = new ArrayList<>(1 << 10);


    public static boolean add(Channel c) {
        log.info("online:{}", c.remoteAddress());
        clients.writeAndFlush(c.remoteAddress() + " online");
        return clients.add(c);
    }

    public static boolean remove(Channel c) {
        boolean remove = false;
        try {
            c.close().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            remove = clients.remove(c);
            log.info("offline:{}", c.remoteAddress());
            clients.writeAndFlush(c.remoteAddress() + " offline");
        }
        return remove;
    }

    public static <T> void foreach(Consumer<? super Channel> consumer) {
        clients.parallelStream().forEach(c -> {
            c.eventLoop().execute(() -> consumer.accept(c));
        });
    }
}
