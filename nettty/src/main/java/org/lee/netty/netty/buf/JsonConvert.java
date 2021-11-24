package org.lee.netty.netty.buf;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.GsonBuilder;

public
class JsonConvert{
    static GsonBuilder gb = new GsonBuilder();
    static {
        gb.disableHtmlEscaping();
    }
    // google的json转化器使用，将Pojo转化为字符串
    public static String pojo2Json(Object o){
        return gb.create().toJson(o);
    }
    // 使用ali的fastJson将字符串转化成为pojo对象
    public static <T>T json2Pojo(String json, Class<T> tClass){
        return JSONObject.parseObject(json, tClass);
    }
}