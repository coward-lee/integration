package org.lee.nettt_in_cation.chapter02.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {

	private final String host;
	private final int port;


	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public static void main(String[] args) throws InterruptedException {
//		if (args.length != 2){
//			System.err.println("usage:"+EchoClient.class.getSimpleName()
//					+"<host><port>"
//			);
//			return;
//		}
		String host = "127.0.0.1";
		int port = 16666;
		new EchoClient(host,port).start();
	}
	public void start() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try{
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					.channel(NioSocketChannel.class)
					.remoteAddress(new InetSocketAddress(host,port))
					.handler(new ChannelInitializer<SocketChannel>() {
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast(new EchoClientHandler());
						}
					});
			ChannelFuture future = bootstrap.connect().sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			group.shutdownGracefully().sync();
			e.printStackTrace();
		}
	}
}
