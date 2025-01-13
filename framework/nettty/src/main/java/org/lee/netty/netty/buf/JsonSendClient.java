package org.lee.netty.netty.buf;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class JsonSendClient {

    String serverIp;
    int serverPort;

    public JsonSendClient(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    static String content = " HHHHHHHHHHH";
    public void runClient(){
        EventLoopGroup worker = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(worker);
            b.channel(NioSocketChannel.class);
            b.remoteAddress(serverIp, serverPort);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldPrepender(4));
                    ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                }
            });
            ChannelFuture f = b.connect();
            f.addListener((ChannelFuture future)->{
                if (future.isSuccess()){
                    System.out.println("客户端连接成功");
                }else{
                    System.out.println("客户端连接失败");
                }
            });
            f.sync();
            Channel channel = f.channel();
            for (int i = 0; i < 1000; i++) {
                JsonMsg user = build(i, i+"->"+content);
                channel.writeAndFlush(user.convertToJson());
                System.out.println("发出报文"+user.convertToJson());
            }
            channel.flush();
            ChannelFuture closeFuture = channel.closeFuture();
            // // 服务监听通道会一直等待关闭的异步任结束 服务监听通道会一直等待关闭的异步任结束
            closeFuture.sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }
    }
    private JsonMsg build(int i, String content){
        return new JsonMsg(i, content);
    }

    public static void main(String[] args) {
        int port = 80;
        String ip = "localhost";
        new JsonSendClient(ip,port).runClient();
    }

}
