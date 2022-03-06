package design.parttern.template;

public abstract class SonyMilk {

    final public void make(){
        select();
        addition();
        soak();
        beat();
    }

    void select(){
        System.out.println("第一步: 选择基本的材料");
    }
    abstract void addition();

    void soak(){
        System.out.println("第三步: 浸泡");
    }
    void beat(){
        System.out.println("第四步: 打豆浆");
    }
}
