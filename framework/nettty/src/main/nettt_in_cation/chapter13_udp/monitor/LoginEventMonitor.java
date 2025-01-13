package org.lee.nettt_in_cation.chapter13_udp.monitor;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

public class LoginEventMonitor {
	private final EventLoopGroup group;
	private final Bootstrap bootstrap;

	public LoginEventMonitor(InetSocketAddress address){
		group = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST,true)
				.handler(new ChannelInitializer<Channel>(){
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new LogEventDecoder());
						pipeline.addLast(new LogEventHandler());
					}
				})
				.localAddress(address);
	}
	public Channel bind(){
		return bootstrap.bind().syncUninterruptibly().channel();
	}
	public void stop(){
		group.shutdownGracefully();
	}

	public static void main(String[] args)  {
		LoginEventMonitor monitor = new LoginEventMonitor(new InetSocketAddress(8080));
		try {
			Channel channel = monitor.bind();
			System.out.println("start monitor");
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			monitor.stop();
		}
	}
}
