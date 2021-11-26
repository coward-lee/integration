package org.lee.nettt_in_cation.chapter13_udp.monitor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.lee.nettt_in_cation.chapter13_udp.LogEvent;

public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LogEvent msg) throws Exception {
		System.out.println(msg.toString());
	}
}
