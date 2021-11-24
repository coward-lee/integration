package org.lee.nettt_in_cation.chapter14;

import io.netty.buffer.ByteBuf;

public class ApnsMessage {
	private static final byte COMMAND = (byte)1;

	public ByteBuf toBuffer(){
		short size  = (short) (
				1 +
						4 + 4+2+32+2
		);
		return null;
	}

}
