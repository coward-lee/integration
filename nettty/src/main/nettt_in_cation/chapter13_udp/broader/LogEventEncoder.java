package org.lee.nettt_in_cation.chapter13_udp.broader;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.lee.nettt_in_cation.chapter13_udp.LogEvent;

import java.net.InetSocketAddress;
import java.util.List;

public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

	private final InetSocketAddress address;

	public LogEventEncoder(InetSocketAddress address) {
		this.address = address;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
		byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);
		byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
		ByteBuf buf = ctx.alloc().buffer(file.length+msg.length+1);
		buf.writeBytes(file);
		buf.writeByte(LogEvent.SEPARATOR);
		out.add(new DatagramPacket(buf, address));
	}
}
