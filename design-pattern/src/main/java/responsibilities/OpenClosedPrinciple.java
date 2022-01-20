package responsibilities;

/**
 * 开闭原则
 * 1. 一个软件的类体类，模块和函数对拓展开放，对修改进行关闭，(对于使用者来说)。用抽象构架框架，用实现拓展细节。
 * 2. 当软件需要变化是，尽量通过拓展软件实体的雷伟来实现变化，为不是通过修改已经有的代码来实现变化。
 */
public class OpenClosedPrinciple {
    public static void main(String[] args) {
        new Graph().draw(new Rectangle());
        new Graph().draw(new Triangle());
    }
}
// 开闭原则的实现
// 拓张的时候只需要再实现一个Shape，并传入Graph就可以了
// 这样我们可以通过拓展Shape，而不会改变Graph的逻辑
class Graph{
    public void draw(Shape shape){
        shape.draw();;
    }
}
abstract class Shape{
    abstract void draw();
}

class Rectangle extends Shape{
    @Override
    void draw() {
        System.out.println("画了：正方形");
    }
}
class Triangle extends Shape{
    @Override
    void draw() {
        System.out.println("画了：三角形");
    }
}
//class Rectangle extends Shape{
//    @Override
//    void draw() {
//        System.out.println("画了：正方形");
//    }
//}
