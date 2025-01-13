package refrence;

import org.junit.Test;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 引用测试
 */
public class RefrenceTest {


    /**
     * 软引用
     * 当内存不足的时候会被回收 ？
     */
    @Test
    public void test_soft_reference() {
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
        String str = "abc";
        SoftReference<String> softReference = new SoftReference<>(str, referenceQueue);
        str = null;
        System.gc();
        System.out.println(softReference.get());
        Reference<? extends String> reference = referenceQueue.poll();
        System.out.println(reference);
        System.out.println(softReference.get());
    }


    /**
     * 内存过大 是否被gc
     * -Xmx200m -Xms200m
     */
    public static void main(String[] args) throws IOException {
        ByteDemo byteDemo = new ByteDemo();
        // 150M

        System.out.println("准备开始");
        System.in.read();
        byteDemo.bytes = new byte[(1 << 20) * 190];
        SoftReference<ByteDemo> softReference = new SoftReference<>(byteDemo);
//        byteDemo = null;
        System.out.println("开始了");
        System.in.read();
        System.gc();
        System.out.println(softReference.get() == null);
    }


    /**
     * 弱引用
     * 使用了gc之后就会立即被回收，再次从原来的弱引用对象获取的话就是获取到null
     */
    @Test
    public void test_weak_reference_01() {
        WeakReference<Byte[]> softReference = new WeakReference<>(new Byte[1 << 20]);
        System.gc();
        System.out.println(softReference.get() == null);
    }


    @Test
    public void test_weak_refrence() {
//        WeakReference<String> weakReference = new WeakReference<>();
//        String str = "abc";
//        SoftReference<String> softReference = new SoftReference<>(str, weakReference);
//        str =  null;
//        System.gc();
//        System.out.println(softReference.get());
    }

    static class ByteDemo {
        byte[] bytes;
    }
}
