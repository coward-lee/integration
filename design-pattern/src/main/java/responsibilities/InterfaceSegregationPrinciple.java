package responsibilities;

/**
 * 接口隔离原则
 * 客户端不应该依赖不需要额接口，即一个类对另一个类的依赖应该建立在最小的接口上
 */
public class InterfaceSegregationPrinciple {

    public static void main(String[] args) {
        A1 a1 = new A1();
        a1.depend1(new I1());
        a1.depend2(new I2());
        a1.depend3(new I2());

        A2 a2 = new A2();
        a2.depend1(new I3());
        a2.depend4(new I3());
        a2.depend5(new I3());
    }

    /**
     * 这样的接口全部聚合再一个interface1上面了，没有讲过对应的接口进行隔离
     */
    interface Interface0 {
        void opt1();
        void opt2();
        void opt3();
        void opt4();
        void opt5();
    }
    static class B implements Interface0 {
        @Override
        public void opt1() {
            print("opt1");
        }

        @Override
        public void opt2() {
            print("opt2");
        }

        @Override
        public void opt3() {
            print("opt3");
        }

        @Override
        public void opt4() {
            print("opt4");
        }

        @Override
        public void opt5() {
            print("opt5");
        }
        void print(String method){
            System.out.println(this.getClass().getSimpleName()+":"+method);
        }
    }
    static class A{
        public void depend1(Interface0 i){
            i.opt1();
        }
        public void depend2(Interface0 i){
            i.opt2();
        }
        public void depend3(Interface0 i){
            i.opt3();
        }
    }
    static class C{
        public void depend1(Interface0 i){
            i.opt1();
        }
        public void depend4(Interface0 i){
            i.opt4();
        }
        public void depend5(Interface0 i){
            i.opt5();
        }
    }


    /**
     * 接口隔离后的实现
     * 将接口拆为三个接口
     */
    interface Interface1{
        void opt1();
    }
    interface Interface2{
        void opt2();
        void opt3();
    }
    interface Interface3{
        void opt4();
        void opt5();
    }
    // 实现的接口的时候根据需要进行实现
    static class I1 implements Interface1{
        @Override
        public void opt1() {
            print("opt1");
        }
        void print(String method){
            System.out.println(this.getClass().getSimpleName()+":"+method);
        }
    }
    // 实现 1，2，3方法，实现接口1，2
    static class I2 implements Interface1, Interface2{
        @Override
        public void opt2() {
            print("opt2");
        }

        @Override
        public void opt3() {
            print("opt3");
        }
        @Override
        public void opt1() {
            print("opt1");
        }
        void print(String method){
            System.out.println(this.getClass().getSimpleName()+":"+method);
        }
    }

    // 实现 1，4，5方法，实现接口1，3
    static class I3 implements Interface1, Interface3{
        @Override
        public void opt1() {
            print("opt1");
        }

        @Override
        public void opt4() {
            print("opt4");
        }

        @Override
        public void opt5() {
            print("opt5");
        }
        void print(String method){
            System.out.println(this.getClass().getSimpleName()+":"+method);
        }
    }
    static class A1{
        public void depend1(Interface1 i){
            i.opt1();
        }
        public void depend2(Interface2 i){
            i.opt2();
        }
        public void depend3(Interface2 i){
            i.opt3();
        }
    }
    static class A2{
        public void depend1(Interface1 i){
            i.opt1();
        }
        public void depend4(Interface3 i){
            i.opt4();
        }
        public void depend5(Interface3 i){
            i.opt5();
        }
    }
}
