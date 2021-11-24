package org.lee.nettt_in_cation.chapter09_test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.junit.Test;

import static org.junit.Assert.*;

public class FixedLengthFrameDecoderTest {


	@Test
	public void testFrameDecoder03(){
		// 分配一个堆缓冲
		ByteBuf buf = Unpooled.buffer();
		for (int i = 0;i<9;i++){
			buf.writeByte(i);
		}
		ByteBuf in = buf.duplicate();
		// 指定每次解码三个字节
		EmbeddedChannel embeddedChannel = new EmbeddedChannel(
				new FixedLengthFrameDecoder(3));
		// 写
		assertTrue(embeddedChannel.writeInbound(in.retain()));
		assertTrue(embeddedChannel.finish());
		// 读
//		readInbound : Return received data from this {@link Channel }
		ByteBuf read = (ByteBuf) embeddedChannel.readInbound();
		assertEquals(buf.readSlice(3),read);
//		System.out.println(read.getInt(0));
		read.release();

		read = (ByteBuf) embeddedChannel.readInbound();
		assertEquals(buf.readSlice(3),read);
		System.out.println(read.toString());
		read.release();

		read = (ByteBuf) embeddedChannel.readInbound();
		assertEquals(buf.readSlice(3),read);
		System.out.println(read.toString());
		read.release();

		assertNull(embeddedChannel.readInbound());
		buf.release();
	}

	@Test
	public void testFrameDecoder02(){
		ByteBuf buf = Unpooled.buffer();
		for (int i = 0;i<9;i++){
			buf.writeByte(i<<9);
		}
		ByteBuf in = buf.duplicate();
		EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
		assertFalse(channel.writeInbound(in.readBytes(2)));
		assertTrue(channel.writeInbound(in.readBytes(7)));

		assertTrue(channel.finish());

		ByteBuf read = (ByteBuf) channel.readInbound();
		assertEquals(buf.readSlice(3),read);
		System.out.println(read.readByte()+","+read.readByte()+","+read.readByte());
		read.release();

		read = (ByteBuf) channel.readInbound();
		assertEquals(buf.readSlice(3),read);
		System.out.println(read.readByte()+","+read.readByte()+","+read.readByte());
		read.release();

		read = (ByteBuf) channel.readInbound();
		assertEquals(buf.readSlice(3),read);
		System.out.println(read.readByte()+","+read.readByte()+","+read.readByte());
		read.release();

		assertNull(channel.readInbound());
		buf.release();

	}

}
