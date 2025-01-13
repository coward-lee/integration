package org.lee.nettt_in_cation.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

/**
 * 清单11-4 P:144
 * 添加压缩http支持
 */

public class HttpCompressionInitializer extends ChannelInitializer<Channel> {
	private final boolean client;

	public HttpCompressionInitializer(boolean client) {
		this.client = client;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		if (client){
			pipeline.addLast("codec", new HttpClientCodec());
			pipeline.addLast("decompressor", new HttpContentCompressor());
		}else{
			pipeline.addLast("codec", new HttpClientCodec());
			pipeline.addLast("compressor", new HttpContentCompressor());
		}
	}
}
