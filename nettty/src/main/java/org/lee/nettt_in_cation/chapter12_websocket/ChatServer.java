package org.lee.nettt_in_cation.chapter12_websocket;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

/**
 * 聊天引导程序
 * 这个东西查个向客户端发送消息的内容
 */
public class ChatServer  {
	private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
	private final EventLoopGroup group = new NioEventLoopGroup();
	private Channel channel;

	public ChannelFuture start(InetSocketAddress address){
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(group)
				.channel(NioServerSocketChannel.class)
				.childHandler(createInitializer(channelGroup));
		ChannelFuture future = bootstrap.bind(address);
		future.syncUninterruptibly();
		channel = future.channel();
		return future;
	}

	public ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup) {
		return  new ChatServerInitializer(channelGroup);
	}
	public void destroy(){
		if (channel!=null){
			channel.close();
		}
		channelGroup.close();
		group.shutdownGracefully();
	}

	public static void main(String[] args) throws CertificateException, SSLException {
//		if (args.length!=1){
//			System.err.println("pxxxxxxxxxxx");
//			System.exit(1);
//		}
		int port = 8080;
		final ChatServer endPoint = new ChatServer();
		ChannelFuture future = endPoint.start(new InetSocketAddress(port));
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				endPoint.destroy();
				System.out.println("结束运行了...");

			}
		});
		System.out.println("服务器正在运行...");

		future.channel().closeFuture().syncUninterruptibly();
//		System.out.println("服务器正在运行...");

	}



}
