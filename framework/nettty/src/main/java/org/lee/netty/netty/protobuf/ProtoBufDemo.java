package org.lee.netty.netty.protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * netty的专用protobuf encoder和 decoder
 * ProtobufEncoder
 * ProtobufDecoder
 * ProtobufVarint32LengthFieldPrepender
 * ProtobufVarint32Decoder
 */
public class ProtoBufDemo {
    public static void main(String[] args) throws Exception {


                new ProtoBufDemo().serAndDesr2();

    }


    public MsgProtos.Msg builder(){
        return MsgProtos.Msg.newBuilder()
                .setContent("content")
                .setId(88889).build();
    }

    // 序列化与反序列化 方式一
    public void serAndDesr0()throws Exception{
        MsgProtos.Msg msg = builder();
        // 将 protobuf转化为二进制字节数组
        // 序列化
        byte[] data = msg.toByteArray();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(data);
        data = outputStream.toByteArray();
        // 反序列化
        MsgProtos.Msg mmsg = MsgProtos.Msg.parseFrom(data);
        System.out.println("id：="+mmsg);
    }


    // 序列化与反序列化 方式二
    // 直接对流进行操作
    public void serAndDesr1()throws Exception{
        MsgProtos.Msg msg = builder();
        // 序列化
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        msg.writeTo(outputStream);

        // 反序列化
        ByteArrayInputStream in  = new ByteArrayInputStream(outputStream.toByteArray());
        MsgProtos.Msg mmsg = MsgProtos.Msg.parseFrom(in);
        System.out.println("id：="+mmsg);
    }
    // 序列化与反序列化 方式二

    // 序列化与反序列化 方式三
    // 带字节长度：【字节长度】【字节数据】，可以解决粘包半包问题
    public void serAndDesr2()throws Exception{
        MsgProtos.Msg msg = builder();
        // 序列化，
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // write*Delimited*To
        msg.writeDelimitedTo(outputStream);

        // 反序列化
        ByteArrayInputStream in  = new ByteArrayInputStream(outputStream.toByteArray());
        MsgProtos.Msg mmsg = MsgProtos.Msg.parseDelimitedFrom(in);
        System.out.println("id：="+mmsg);
    }

}
