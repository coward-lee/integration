package design.parttern.flyweight;

public class ConcreteWebsite extends Website{
    String type = ""; // 网站的发布形式

    public ConcreteWebsite(String type) {
        this.type = type;
    }

    @Override
    public void use(User user) {
        System.out.println("网站的发布形式是:"+type+ " 使用者是:"+user.getName());
    }
}
