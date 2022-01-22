package org.lee.study.spring.recycle;

public class Extend extends Parent<Extend.InClass> {
    public void print(){
        System.out.println(get(new InClass()).name);
    }

    String extendName = "extendName";
    public InClass create(){
        return new InClass();
    }


    class InClass{
        String name = "InClass";
        public InClass() {
            System.out.println(Extend.this.extendName);
        }
        public void doSomeThing(){
            System.out.println(Extend.this.extendName);

        }

    }
}
