package org.lee.nettt_in_cation.chapter12_websocket;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.ImmediateEventExecutor;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

/**
 * 聊天引导程序
 * 这个东西查个向客户端发送消息的内容
 */
public class SecureChatServer extends ChatServer {

	private  final SslContext context;

	public SecureChatServer(SslContext context) {
		this.context = context;
	}




	public ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup) {
		return  new SecureChatServerInitializer(channelGroup,context);
	}



	public static void main(String[] args) throws CertificateException, SSLException {
//		if (args.length!=1){
//			System.err.println("pxxxxxxxxxxx");
//			System.exit(1);
//		}
		int port = 8080;
		SelfSignedCertificate certificate = new SelfSignedCertificate();
		SslContext context = SslContext.newServerContext(certificate.certificate(),certificate.privateKey());
		final SecureChatServer endPoint = new SecureChatServer(context);
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
