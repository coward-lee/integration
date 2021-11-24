package org.lee.netty.netty.buf;


public class JsonMsg{
    private int id;
    private String name;

    public JsonMsg(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "JsonMsg{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 使用google的序列化
    public String convertToJson(){
        return JsonConvert.pojo2Json(this);
    }

    // 使用阿里的反序列化
    public static JsonMsg parseFromJson(String json){
        return JsonConvert.json2Pojo(json, JsonMsg.class);
    }
}
