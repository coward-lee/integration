package org.lee.nettt_in_cation.chapter02.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

	private final int port;

	public EchoServer(int port){
		this.port = port;
	}

	public static void main(String[] args) throws InterruptedException {
//		if (args.length!=1){
//			System.err.println("usage:"+EchoServer.class.getSimpleName()+
//					"<port>"
//			);
//			return;
//		}
		int port = 16666;
		new EchoServer(port).start();
	}
	public void start() throws InterruptedException {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(group)
					.channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast(new EchoServerHandler());
						}
					});
			ChannelFuture future = b.bind().sync();
			System.out.println(EchoServer.class.getName() + " started and listen on " + future.channel().localAddress());
			future.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			group.shutdownGracefully().sync();
			e.printStackTrace();
		}
	}
}
