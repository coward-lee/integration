package design.parttern.flyweight;

public class FlyweightMain {
    public static void main(String[] args) {
        WebsiteFactory websiteFactory = new WebsiteFactory();
        Website news = websiteFactory.getWebSiteCategory("新闻");
        news.use(new User("新浪"));
        Website blog = websiteFactory.getWebSiteCategory("blog");
        blog.use(new User("stack overflow"));
        Website bbs = websiteFactory.getWebSiteCategory("bbs");
        bbs.use(new User("..."));
        Website xxx = websiteFactory.getWebSiteCategory("xxx");
        xxx.use(new User("cccc"));
        System.out.println("总数:" + websiteFactory.getWebSiteCount());
    }
}
