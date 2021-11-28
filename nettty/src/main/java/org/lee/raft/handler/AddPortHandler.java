package org.lee.raft.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.unix.DatagramSocketAddress;

import java.util.Set;

class  AddPortHandler extends ChannelInboundHandlerAdapter {
    Set<Integer> ports;

    public AddPortHandler(Set<Integer> ports) {
        this.ports = ports;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramSocketAddress datagramSocketAddress = (DatagramSocketAddress) ctx.channel().remoteAddress();
        int port = datagramSocketAddress.getPort();
        ports.add(port);
        super.channelRead(ctx, msg);
    }
}
