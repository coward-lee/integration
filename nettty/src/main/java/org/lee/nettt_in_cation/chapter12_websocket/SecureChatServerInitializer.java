package org.lee.nettt_in_cation.chapter12_websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.net.ssl.SSLEngine;

public class SecureChatServerInitializer extends ChatServerInitializer {
	private final SslContext context;
	public SecureChatServerInitializer(ChannelGroup group,SslContext sslContext) {
		super(group);
		this.context = sslContext;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		super.initChannel(ch);
		SSLEngine engine = context.newEngine(ch.alloc());
		engine.setUseClientMode(false);
		ch.pipeline().addFirst(new SslHandler(engine));
	}
}
