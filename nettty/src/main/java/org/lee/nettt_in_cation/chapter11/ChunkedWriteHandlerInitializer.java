package org.lee.nettt_in_cation.chapter11;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;


/**
 * 零拷贝实现文件的传输
 */
public class ChunkedWriteHandlerInitializer  extends ChannelInitializer<Channel> {
	private final File file;
	private final SslContext sslContext;

	public ChunkedWriteHandlerInitializer(File file, SslContext sslContext) {
		this.file = file;
		this.sslContext = sslContext;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new SslHandler(sslContext.newEngine(ch.alloc())));
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new WriteStreamHandler());
	}

	public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
			ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
		}
	}
}
