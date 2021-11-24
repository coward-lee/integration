package org.lee.nettt_in_cation.chapter10_decoder_and_encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

public class CharToByteEncoder extends MessageToByteEncoder<Character> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out) throws Exception {
		out.writeChar(msg);
	}
}
