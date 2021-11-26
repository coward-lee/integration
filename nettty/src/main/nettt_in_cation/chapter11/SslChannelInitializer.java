package org.lee.nettt_in_cation.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 11-1
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
	private final SslContext sslContext;
	private final boolean startTls;

	public SslChannelInitializer(SslContext sslContext, boolean startTls) {
		this.sslContext = sslContext;
		this.startTls = startTls;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		SSLEngine engine = sslContext.newEngine(ch.alloc());
		ch.pipeline().addFirst("ssl",new SslHandler(engine,startTls));
	}
}
