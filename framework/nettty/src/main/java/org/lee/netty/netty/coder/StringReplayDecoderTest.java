package org.lee.netty.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.lee.netty.RandomUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class StringReplayDecoderTest {

    static String content = "abcdefghijklmn";

    public static void main(String[] args) throws Exception {
        new StringReplayDecoderTest().testStringReplayHandler();
    }

    public void testStringReplayHandler() throws Exception {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new StringReplayHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        byte[] bytes = content.getBytes("UTF-8");
        for (int j = 0; j < 100; j++) {
            int random = RandomUtil.get(4);
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(bytes.length*random);
            for (int k = 0; k < random; k++) {
                buf.writeBytes(bytes);
            }
            channel.writeInbound(buf);
        }
        Thread.sleep(Integer.MAX_VALUE);

    }
}

class StringReplayHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String s = (String) msg;
        System.out.println("xxxxxxxxxx:["+s+"]");
    }
}

class StringReplayDecoder extends ReplayingDecoder<StringReplayDecoder.Status> {
    enum Status{
        PARSE_1,PARSE_2
    }

    private int length;
    private byte[] inBytes;
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()){
            case PARSE_1:
                length = in.readInt();
                inBytes = new byte[length];
                break;
            case PARSE_2:
                 in.readBytes(inBytes,0,length);
                 out.add(new String(inBytes, StandardCharsets.UTF_8));
                break;
        }
    }
}
