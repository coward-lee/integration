package responsibilities;

/**
 *
 * 基本介绍
 * 1)里氏替换原则(Liskov Substitution Principle)在1988年，由麻省理工学院的以为姓里的女士提出的。
 * 2）如果对每个类型为T1的对象o1，都有类型为T2的对象o2，使得以T1定义的所有程序P在所有的对象o1都代换成o2时，
 *  程序P的行为没有发生变化，那么类型T2是类型T1的子类型。换句话说，所有引用基类的地方必须能透明地使用其子类的对象。
 *  就是使用父类，同时可以随意替换实现的子类
 * 3)在使用继承时，遵循里氏替换原则，在子类中尽量不要重写父类的方法
 * 4)里氏替换原则告诉我们，继承实际上让两个类耦合性增强了，在适当的情况下，可以通过聚合, 组合, 依赖来解决问题。
 */
public class LiShiPrincipal {
    public static void main(String[] args) {
        System.out.println(new B().add(2,3));
        // 由于BaseA类不再继承BaseB类，因此调用者不会认为原来的add是最初的加法了，
        // 这样可以把函数调用的功能变得更加明确了
        BaseA baseA = new BaseA();
        System.out.println(baseA.add(1,2));
        // 使用组合仍然可以通过BaseA调用BaseB的方法
        System.out.println(baseA.addB(1,2));
    }

}
class A{
    public int add(int a, int b){
        return a+b;
    }
}

class B extends A{
    @Override
    public int add(int a, int b) {
        return a+b+99;
    }
}

// 改进
class Base{
    public int add(int a, int b){
        return a+b;
    }
}

class BaseB extends Base{
    @Override
    public int add(int a, int b) {
        return a + b + 99;
    }
}

class BaseA extends Base{
    BaseB BaseB = new BaseB();
    @Override
    public int add(int a, int b) {
        return a + b;
    }
    public int addB(int a, int b){
        return BaseB.add(a,b);
    }
}
