package design.parttern.template.improve;

public class ImproveMain {
    public static void main(String[] args) {
        SonyMilk sonyMilk = new PeanutSoyaMilk();
        sonyMilk.make();
        new RedBeanSoyaMilk().make();
        new PureSoyaMilk().make();
    }
}
