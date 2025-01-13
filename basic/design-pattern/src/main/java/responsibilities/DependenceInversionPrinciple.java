package responsibilities;


/**
 * 依赖倒转原则
 * 1. 高层模块不应该依赖底层模块，二者都应该依赖其抽象，
 * 2. 抽象不应该依赖细节，细节应该抽象
 * 3. 依赖倒置的中心思想是面向接口编程
 * 4. 以抽象为继承进行设计，
 * 5. 接口指定规范，细节有实现类进行操作
 * 注意事项和细节
 * 1. 底层模块尽量都要有抽象类或者接口，这样程序的稳定性更好
 * 2. 变量声明类尽量是抽象类或接口，这样我们的变量引用和实际对象间，就存在一个缓冲层，利于程序拓展和优化
 * 3. 继承的时候需要遵守里氏替换原则
 *
 * 基本介绍
 * 1)里氏替换原则(Liskov Substitution Principle)在1988年，由麻省理工学院的以为姓里的女士提出的。
 * 2）如果对每个类型为T1的对象o1，都有类型为T2的对象o2，使得以T1定义的所有程序P在所有的对象o1都代换成o2时，
 *  程序P的行为没有发生变化，那么类型T2是类型T1的子类型。换句话说，所有引用基类的地方必须能透明地使用其子类的对象。
 *  就是使用父类，同时可以随意替换实现的子类
 * 3)在使用继承时，遵循里氏替换原则，在子类中尽量不要重写父类的方法
 * 4)里氏替换原则告诉我们，继承实际上让两个类耦合性增强了，在适当的情况下，可以通过聚合, 组合, 依赖来解决问题。
 */
public class DependenceInversionPrinciple {
    public static void main(String[] args) {
        Person person = new Person();
        person.receive(new Email("邮件消息"));
        person.receive(new WeiXin());
        person.receive(new Phone());
        //
        new PhoneWithConstructor(new Implementation()).open();
    }
}


// 完成一个Person接受接受功能的消息
// 方式1：直接来
class Person{
    public void receive(Email email){
        System.out.println("收到了 email:"+email.info);
    }
    // 优化后的接收方法
    public void receive(IReceiver iReceiver){
        System.out.println("收到了："+iReceiver.getInfo());
    }
}
class Email{
    public Email(String info) {
        this.info = info;
    }

    String info;
}
// 方式2：利用依赖倒转原则优化方案
// 通过接口实现依赖的传递
interface IReceiver{
    String getInfo();
}

class WeiXin implements IReceiver{
    @Override
    public String getInfo() {
        return "微信的消息发过来了";
    }
}

class Phone implements IReceiver{
    @Override
    public String getInfo() {
        return "手机发的消息发过来了";
    }
}

// 方式三：通过构造方法传递依赖
// 同样也可以通过setter实现
interface IReceiverWithConstructor{
    void getInfo();
}
class Implementation implements IReceiverWithConstructor{
    @Override
    public void getInfo() {
        System.out.println("一个实现方法");
    }
}
class WeiXinWithConstructor {
    IReceiverWithConstructor iReceiver;

    public WeiXinWithConstructor(IReceiverWithConstructor iReceiver) {
        this.iReceiver = iReceiver;
    }

    public void setiReceiver(IReceiverWithConstructor iReceiver) {
        this.iReceiver = iReceiver;
    }

    public void open() {
        iReceiver.getInfo();
    }
}

class PhoneWithConstructor {
    IReceiverWithConstructor iReceiver;
    public PhoneWithConstructor(IReceiverWithConstructor iReceiver) {
        this.iReceiver = iReceiver;
    }

    public void setiReceiver(IReceiverWithConstructor iReceiver) {
        this.iReceiver = iReceiver;
    }

    public void open() {
        System.out.println("手机启动");
        iReceiver.getInfo();
    }
}
