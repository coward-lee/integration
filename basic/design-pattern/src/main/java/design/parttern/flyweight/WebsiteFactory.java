package design.parttern.flyweight;

import java.util.HashMap;

// 网站工厂类,根据需要返回一个类型
public class WebsiteFactory {
    // 集合

    private HashMap<String, ConcreteWebsite> pool = new HashMap<>();

    // 根据类型返回网站
    public Website getWebSiteCategory(String type){
        if (!pool.containsKey(type)){
            pool.put(type, new ConcreteWebsite(type));
        }
        return pool.get(type);
    }

    public int getWebSiteCount(){
        return pool.size();
    }
}
