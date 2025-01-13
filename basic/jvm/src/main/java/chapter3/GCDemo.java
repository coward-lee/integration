package chapter3;

import org.junit.Test;

public class GCDemo {
//  没有输出gc回收的信息
    public static void main(String[] args) {

        ReferenceA referenceA = new ReferenceA();
        ReferenceB referenceB = new ReferenceB();
        referenceA.instance = referenceB;
        referenceB.instance = referenceA;
        ReferenceA referenceA1 = new ReferenceA();
        System.gc();;
        System.out.println();
    }

    //  没有输出gc回收的信息
    @Test
    public void testGCDemo01(){
        ReferenceA referenceA = new ReferenceA();
        ReferenceB referenceB = new ReferenceB();
        referenceA.instance = referenceB;
        referenceB.instance = referenceA;
        ReferenceA referenceA1 = new ReferenceA();
        System.gc();;
        System.out.println();

    }


    static class ReferenceA{
        ReferenceB instance;
    }
    static class ReferenceB{
        ReferenceA instance;
    }

}
