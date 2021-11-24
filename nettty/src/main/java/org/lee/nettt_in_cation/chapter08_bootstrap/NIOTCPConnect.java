package org.lee.nettt_in_cation.chapter08_bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioDatagramChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import org.junit.Test;

import java.net.InetSocketAddress;

public class NIOTCPConnect {


	/**
	 * Nio链接tcp的一个测试 客户端的bootstrap
	 * 清单8-1 p:102
	 */
	@Test
	public void testNIOTCPConnectBootstrap(){
		System.out.println("开始");
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.handler(new SimpleChannelInboundHandler<ByteBuf>() {
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
						System.out.println("received data");
					}
				});
		ChannelFuture future = bootstrap.connect(
				new InetSocketAddress("www.baidu.com",80));
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()){
					System.out.println("connection establish");
				}else{
					System.out.println("connection failed");
					future.cause().fillInStackTrace();
				}
			}
		});
		System.out.println("结束");

	}


	/**
	 * 不兼容的测试
	 * 清单8-3 p:104
	 */
	@Test
	public void testIncompatibility(){
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(OioSocketChannel.class)
				.handler(new SimpleChannelInboundHandler<ByteBuf>() {
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
						System.out.println("received data");
					}
				});
		ChannelFuture future = bootstrap.connect(
				new InetSocketAddress("www.manning.com",80));
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()){
					System.out.println("connection establish");
				}else{
					System.out.println("connection failed");
					future.cause().printStackTrace();
				}
			}
		});
	}



	/**
	 * Nio链接tcp的一个测试 服务端的bootstrap
	 * 清单8-4 p:106
	 * 每个Channel对应一个EventLoop
	 */
	@Test
	public void testNIOTCPConnectServerBootstrap01(){
		NioEventLoopGroup group = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(group)
				.channel(NioServerSocketChannel.class)
				.childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
						System.out.println("data received");
					}
				});
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()){
					System.out.println("server bound");
				}else{
					System.out.println("server did not bind");
					future.cause().printStackTrace();
				}
			}
		});
	}

	/**
	 * Nio链接tcp的一个测试 服务端的bootstrap
	 * 清单8-5 p:107
	 * 多个个Channel共享一个EventLoop，使用EventLoop而不是使用多线程。
	 * 但是只是添加单个channel到eventLoop中
	 */
	public void testNIOTCPConnectServerBootstrap02(){
		ServerBootstrap bootstrap = new ServerBootstrap();
		/*
		 * Set the {@link EventLoopGroup} for the parent (acceptor) and the child (client). These
		 * {@link EventLoopGroup}'s are used to handle all the events and IO for {@link ServerChannel} and
		 * {@link Channel}'s.
		 * 设置这个父类接受者和客户端的事件池组。这些组会用来处理所有的服务channel和channel的事件和IO
		 */
		bootstrap.group(new NioEventLoopGroup(),new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
					ChannelFuture future;
					@Override
					public void channelActive(ChannelHandlerContext ctx)  {
						//用于连接远程的bootstrap
						Bootstrap bootstrap = new Bootstrap();
						// 指定到对应的channel
						bootstrap.channel(NioSocketChannel.class)
								.handler(new SimpleChannelInboundHandler<ByteBuf>() {
									@Override
									protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)  {
										System.out.println("data received");
									}
								});
						// 尽量使用eventLoop 减少多线程的使用
						//==============================================================================================
						bootstrap.group(ctx.channel().eventLoop());
						future = bootstrap.connect("www.baidu.com",80);
					}

					@Override
					protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
						if (future.isDone()){
							System.out.println("data received");
						}
					}
				});
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				if (channelFuture.isSuccess()) {
					System.out.println("server bound");
				}else{
					System.out.println("bound failed");
					channelFuture.cause().printStackTrace();
				}
			}
		});
	}


	/**
	 * Nio链接tcp的一个测试 服务端的bootstrap
	 * 清单8-6 p:109
	 * 多个个Channel共享一个EventLoop，使用EventLoop而不是使用多线程。
	 * 但是只是添加多个channel到eventLoop中
	 * 为应用程序安装多个ChannelHandler，定义自己的ChannelInitializer实现来将他们安装到ChannelPipeline中
	 */

	public void testNIOTCPConnectServerBootstrap03() throws InterruptedException {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(new NioEventLoopGroup(),new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializerImpl());
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
		future.sync();

	}
	/**
	 * 义自己的ChannelInitializer实现来将他们安装到ChannelPipeline中
	 */
	static final class ChannelInitializerImpl extends ChannelInitializer<Channel> {

		@Override
		protected void initChannel(Channel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new HttpClientCodec());
			pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
		}
	}


	/**
	 * ChannelOption来引导并配置一个channel
	 * 清单8-7 p:110
	 */
	@Test
	public void testNIOTCPConnectChannelOption()  {
		final AttributeKey<Integer> id = AttributeKey.newInstance("ID");
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new NioEventLoopGroup())
				.channel(NioSocketChannel.class)
				.handler(new SimpleChannelInboundHandler<ByteBuf>() {
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
						System.out.println("data received");
					}

					@Override
					public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
						Integer idValue = ctx.channel().attr(id).get();
						System.out.println("attr : "+idValue.toString());
					}
				});
		// 使用ChannelOption
		bootstrap.option(ChannelOption.SO_KEEPALIVE,true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000);
		bootstrap.attr(id,12346);
		ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.manning.com",80));
//		注册动作
		future.syncUninterruptibly();
	}

	/**
	 * 引导DatagramChannel
	 * 清单8-8 p:111
	 */

	public void testNIONonlinkingConnectChannelOption()  {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(new OioEventLoopGroup())
				.channel(OioDatagramChannel.class)
				.handler(new SimpleChannelInboundHandler<DatagramPacket>() {
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
						System.out.println("data received");
					}

				});

		// 无连接所以不用connect
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(0));
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future)  {
				if (future.isSuccess()){
					System.out.println("done");
				}else{
					System.out.println("failed");
				}
			}
		});
//		注册动作
		future.syncUninterruptibly();
	}

	public void testClose() throws InterruptedException {
		NioEventLoopGroup group = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(group)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializerImpl());
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
		future.sync();
		Future<?> closeFuture = group.shutdownGracefully();
		closeFuture.syncUninterruptibly();
	}
}
