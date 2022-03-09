package org.lee.study.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 泛型相关的内容
 */
public class TypeDemo {
    public static void main(String[] args) {
//        var opt = new Optional<String>[10]; // error
    }
    //  在于到 下面这种情况可以是用  @SafeVarargs 和 @SuppressWarnings("unchecked") 来解决警告
//    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T extends Object> void addAll(Collection<T> col, T... ts){
        System.out.println(ts.length);
        // 不能实例化泛型
//        T t = new T();  //Type parameter 'T' cannot be instantiated directly
        // 类型擦除会将其擦除为Object  因为 <T> 的 写法等同于 <T extend Object>
        // 而类型擦除会将其擦除为顶级父类
        // 也不能直接使用这样的反射形式来实现
//        T t = T.class.getConstructor().newInstance();   //Cannot select from a type variable
        List<A> list1 = new ArrayList<>();
        list1.add(new B());
        A a = list1.get(0);

    }
    <T extends A> void test1(T arg){
        arg.doSomething();
    }
    static class A {
        public void doSomething(){
            System.out.println("a : do ");
        }

    }
    static class B extends A{
        @Override
        public void doSomething(){
            System.out.println("B : do ");
        }
    }

    // 如果想要得到实例化对象也是使用如下形式
    static class Pair<T>{
        T obj;
        private Pair() {
        }

        private Pair(T obj) {
            this.obj = obj;
        }
        //可以使用如下的形式
        public static <T> Pair<T> makeObj1(Supplier<T> supplier){
            return new Pair<T>(supplier.get());
        }
        // 或者
        public static <T> Pair<T> makeObj2(Class<T> clazz){
            try {
                return new Pair<T>(clazz.getConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
