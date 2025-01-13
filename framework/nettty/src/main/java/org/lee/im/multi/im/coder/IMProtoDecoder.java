package org.lee.im.multi.im.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.lee.im.multi.im.domain.MessageProto.Message;
import org.lee.im.multi.im.domain.MessageProto;

import java.util.List;

import static org.lee.im.multi.im.coder.Coder.TYPE_BYTE_SIZE;

@Slf4j
public class IMProtoDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 标记当前读指针的位置，即readIndex的位置
        in.markReaderIndex();
        if (in.readableBytes() < TYPE_BYTE_SIZE){
//            log.warn("message length info is not enough");
            return;
        }
        int length = in.readInt();

        if (length < 0){
//            log.warn("message is illegal");
            ctx.close();
        }
        if (length > in.readableBytes()) {
            // 发过来的内容长度少于消息原来的的长度（length）
            // 则重置读取位置，下次再此读取
            in.resetReaderIndex();
            return;
        }
        byte[] bytes;
        if (in.hasArray()){
            // 堆缓冲区
            ByteBuf slice = in.slice();
            bytes = slice.array();
        }else{
            // 直接缓冲区
            bytes = new byte[length];
            in.readBytes(bytes,0, length);
        }

        Message message = MessageProto.Message.parseFrom(bytes);
        if (message!=null){
            out.add(message);
        }
    }
}
