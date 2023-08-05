package org.lee.aiguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * 1. SimpleChannelInboundHandler 继承自 ChannelInboundHandlerAdapter
 * 2. HttpObject 是客户端和服务器端相互通信的数据被封装成HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final Logger log = LoggerFactory.getLogger(TestHttpServerHandler.class);
    // 读取客户端的消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest){
            log.info("pipeline hash code :{}",ctx.pipeline().hashCode());

            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());
            if ("/favicon.ico".equals(uri.getPath())){
                log.info("/favicon.ico uri");
                return;
            }else{
                log.info("msg type is {}",msg.getClass());
                log.info("client is from {}",ctx.channel().remoteAddress());
            }

            // 回复信息给浏览器[http协议]

            // 构造一个httpResponse
            ByteBuf content = Unpooled.copiedBuffer("hello, i am http server, 中文版本", StandardCharsets.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            ctx.writeAndFlush(response);
        }
    }
}
