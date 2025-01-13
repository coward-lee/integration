package design.parttern.template.improve;

public class RedBeanSoyaMilk extends SonyMilk {
    @Override
    void addition() {
        System.out.println("第二步:添加了红豆");
    }

    @Override
    boolean customWantAdd() {
        return false;
    }
}
