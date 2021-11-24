package org.lee.nettt_in_cation.chapter11;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import sun.plugin2.message.Message;


/**
 * google的protocol buffer
 */
public class ProtoBufInitializer extends ChannelInitializer<Channel> {

	private final MessageLite lite;

	public ProtoBufInitializer(MessageLite lite) {
		this.lite = lite;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new ProtobufVarint32FrameDecoder());
		pipeline.addLast(new ProtobufEncoder());
		pipeline.addLast(new ProtobufDecoder(lite));
		pipeline.addLast(new MarshallingInitializer.ObjectHandler());
	}
}
