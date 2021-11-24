package org.lee.nettt_in_cation.chapter10_decoder_and_encoder;

import io.netty.channel.CombinedChannelDuplexHandler;

public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
	public CombinedByteCharCodec() {
		super(new ByteToCharDecoder(),new CharToByteEncoder());
	}
}
