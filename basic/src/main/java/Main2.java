import org.lee.study.util.FieldUtil;

public class Main2 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        String s1 = "hello";
        String s2 = new String("hello");

        System.out.println(s1 == s2);
    }


    void method(){
        //        String s1 = "hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohelloxxxxxxxxxxxxxxxxxxxxxxxx";
        String s1 = "hello";
        String s2 = new String("hello");
        String s0 = "hel";
        String s3 = s0 + "lo";
        String s4 = new String("hel") + "lo";
        String s31 = (s0 + "lo").intern();
        String s42 = (new String("hel") + "lo").intern();
//        System.out.println("s1==s2 is " + ( == s2));
        System.out.println("s1 " + FieldUtil.getValue(String.class, "value", s1));
        System.out.println("s2 " + FieldUtil.getValue(String.class, "value", s2));
        System.out.println("s0 " + FieldUtil.getValue(String.class, "value", s0));
        System.out.println("s3 " + FieldUtil.getValue(String.class, "value", s3));
        System.out.println("s4 " + FieldUtil.getValue(String.class, "value", s4));
        System.out.println("s31" + FieldUtil.getValue(String.class, "value", s31));
        System.out.println("s42" + FieldUtil.getValue(String.class, "value", s42));
    }


}
