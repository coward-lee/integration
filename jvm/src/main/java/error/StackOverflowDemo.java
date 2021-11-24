package error;

public class StackOverflowDemo {
    public static void main(String[] args) {
        new StackOverflowErrorDemo().start();
        System.out.println("主线程初始化完成：");
    }

// 虚拟机栈溢出
    static class StackOverflowErrorDemo extends Thread{
        @Override
        public void run() {
            over();
        }
        public void over(){
            System.out.println("???????????????");
            over();
        }
    }
}
