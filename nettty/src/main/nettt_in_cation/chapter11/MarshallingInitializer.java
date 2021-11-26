package org.lee.nettt_in_cation.chapter11;

import io.netty.channel.*;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import java.io.Serializable;
import java.security.PublicKey;

public class MarshallingInitializer extends ChannelInitializer<Channel> {
	private final MarshallerProvider provider;
	private final UnmarshallerProvider unmarshallerProvider;

	public MarshallingInitializer(MarshallerProvider provider, UnmarshallerProvider unmarshallerProvider) {
		this.provider = provider;
		this.unmarshallerProvider = unmarshallerProvider;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new MarshallingDecoder(unmarshallerProvider));
		pipeline.addLast(new MarshallingEncoder(provider));
		pipeline.addLast(new ObjectHandler());
	}

	public static final class ObjectHandler extends SimpleChannelInboundHandler<Serializable>{
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) throws Exception {
			System.out.println(msg);
		}
	}
}
