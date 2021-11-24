package org.lee.nettt_in_cation.chapter05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ByteBufDemo  extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf)msg;
		for (int i= 0;i<byteBuf.capacity();i++){
			byte b = byteBuf.getByte(i);
			System.out.println(b);
			byteBuf.readerIndex(2);
		}
	}

	@Test
	public void testGetAndSet(){
		Charset charset = StandardCharsets.UTF_8;
		ByteBuf buf =  Unpooled.copiedBuffer("xxxxxx", charset);
		int reI = buf.readerIndex();
		int wrI = buf.writerIndex();
		System.out.println((char)buf.getByte(0));
		buf.setByte(0,'1');
		System.out.println(reI == buf.readerIndex());
		System.out.println(wrI == buf.writerIndex());
		System.out.println((char)buf.getByte(0));

		System.out.println(buf.writableBytes());
		System.out.println(buf.readableBytes());
		buf.release();

	}
}
