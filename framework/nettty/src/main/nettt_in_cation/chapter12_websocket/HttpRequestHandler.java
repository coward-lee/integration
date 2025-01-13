package org.lee.nettt_in_cation.chapter12_websocket;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final String wsUri;
	private static final File INDEX;

	static{
		try {
			URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();

			String path = location.toURI()+"index.html";
//			writeIndexHTML(path);
			path = !path.contains("file:") ? path : path.substring(5);
			INDEX = new File(path);
		} catch (URISyntaxException e) {
//			e.printStackTrace();
			throw new IllegalStateException("Unable to locate index.html",e);
		}
	}

	public HttpRequestHandler(String wsUri) {
		this.wsUri = wsUri;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		// 判断是否请求了websocket协议升级
		if (wsUri.equalsIgnoreCase(msg.uri())){
			ctx.fireChannelRead(msg.retain());

		}else{
			if (HttpUtil.is100ContinueExpected(msg)){
				send1000Continue(ctx);
			}
//			/*String*/ path = INDEX.getAbsolutePath();
			RandomAccessFile file = new RandomAccessFile(INDEX,"r");
			HttpResponse response = new DefaultFullHttpResponse(
					msg.getProtocolVersion(),HttpResponseStatus.OK
			);
			response.headers().set(HttpHeaders.Names.CONTENT_TYPE,"text/html;charset=UTF-8");
			boolean keepAlive = HttpHeaders.isKeepAlive(msg);
			if (keepAlive){
				response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,file.length());
				response.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
			}
			ctx.write(response);
			if (ctx.pipeline().get(SslHandler.class) == null){
				ctx.write(new DefaultFileRegion(file.getChannel(),0,file.length()));
			}else{
				ctx.write(new ChunkedNioFile(file.getChannel()));
			}
			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			if (!keepAlive){
				future.addListener(ChannelFutureListener.CLOSE);
			}
		}
	}


	public static void send1000Continue(ChannelHandlerContext channelHandlerContext){
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.CONTINUE);
		channelHandlerContext.writeAndFlush(response);
	}
	public static void writeIndexHTML(String path){
		File file = new File(path);
		try {
			FileWriter writer = null;
			if (file.exists()){
				writer = new FileWriter(file);
			}else{
				boolean created = file.createNewFile();
				if (created){
					writer = new FileWriter(file);
				}
			}
			if (writer !=null) writer.write("hello");
		} catch ( IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
