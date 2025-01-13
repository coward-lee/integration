package design.pattern;
// 使用静态内部类实现单例模式
public class SingleTon {

    static class LazyHolder{
        private static final SingleTon INSTANCE = new SingleTon();
    }
    private SingleTon(){

    }
    public static SingleTon getInstance(){
        return LazyHolder.INSTANCE;
    }
}
