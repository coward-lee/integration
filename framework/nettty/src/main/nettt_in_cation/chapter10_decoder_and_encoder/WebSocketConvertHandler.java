package org.lee.nettt_in_cation.chapter10_decoder_and_encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

public class WebSocketConvertHandler  extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {


	/**
	 * 编码
	 * @param ctx
	 * @param msg
	 * @param out
	 * @throws Exception
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {
		ByteBuf payLoad = msg.getData().duplicate().retain();
		switch (msg.getType()){
			case BINARY:
				out.add(new BinaryWebSocketFrame(payLoad));
				break;
			case CLOSE:
				out.add(new CloseWebSocketFrame(true,0,payLoad));
				break;
			case CONTINUATION:
				out.add(new ContinuationWebSocketFrame(payLoad));
				break;
			case PING:
				out.add(new PingWebSocketFrame(payLoad));
				break;
			case PONG:
				out.add(new PongWebSocketFrame(payLoad));
				break;
			case TEXT:
				out.add(new TextWebSocketFrame(payLoad));
				break;
			default:
				throw new IllegalStateException("Unsupported websocket msg "+msg);
		}
	}

	/**
	 *
	 * @param ctx
	 * @param msg
	 * @param out
	 * @throws Exception
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
		ByteBuf buf = msg.content().duplicate().retain();
		if (msg instanceof BinaryWebSocketFrame){
			out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.BINARY,buf));
		}else if (msg instanceof CloseWebSocketFrame){
			out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CLOSE,buf));
		}else if (msg instanceof ContinuationWebSocketFrame){
			out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.CONTINUATION,buf));
		}else if (msg instanceof PingWebSocketFrame){
			out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PING,buf));
		}else if (msg instanceof PongWebSocketFrame){
			out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.PONG,buf));
		}else if (msg instanceof TextWebSocketFrame){
			out.add(new MyWebSocketFrame(MyWebSocketFrame.FrameType.TEXT,buf));
		}
	}

	public static final class MyWebSocketFrame{
		public enum FrameType{
			BINARY,
			CLOSE,
			PING,
			PONG,
			TEXT,
			CONTINUATION,
		}
		private final FrameType type;
		private final ByteBuf data;

		public MyWebSocketFrame(FrameType type, ByteBuf data) {
			this.type = type;
			this.data = data;
		}

		public FrameType getType() {
			return type;
		}

		public ByteBuf getData() {
			return data;
		}
	}
}


