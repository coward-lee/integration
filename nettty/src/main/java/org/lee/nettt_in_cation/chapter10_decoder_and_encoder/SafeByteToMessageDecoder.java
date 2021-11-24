package org.lee.nettt_in_cation.chapter10_decoder_and_encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

public class SafeByteToMessageDecoder extends ByteToMessageDecoder {
	private static final int MAX_LEN  = 1024;
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int i = in.readableBytes();
		if (i>MAX_LEN){
			in.skipBytes(i);
			throw new TooLongFrameException("Frame too big! 帧太长了");
		}
	}
}
