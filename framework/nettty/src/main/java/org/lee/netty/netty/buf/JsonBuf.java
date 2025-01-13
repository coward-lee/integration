package org.lee.netty.netty.buf;

import org.junit.Test;

public class JsonBuf {



    public static void main(String[] args) {

        JsonMsg m = new JsonMsg(999,"aaaaa");

        String json = m.convertToJson();
        System.out.println(json);
        JsonMsg jsonMsg = JsonMsg.parseFromJson(json);
        System.out.println(jsonMsg.getId());
        System.out.println(jsonMsg.getName());
        System.out.println("我的天啦");
    }
    @Test
    public void test(){

        JsonMsg m = new JsonMsg(999,"aaaaa");

        String json = m.convertToJson();
        System.out.println(json);
        JsonMsg jsonMsg = JsonMsg.parseFromJson(json);
        System.out.println(jsonMsg.getId());
        System.out.println(jsonMsg.getName());
        System.out.println("我的天啦");
    }


//    public LengthFieldPrepender(int lengthFieldLength){
//
//    }

}

