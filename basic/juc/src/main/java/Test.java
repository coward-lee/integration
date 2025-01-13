

public class Test{
//    9
//    19
//    29
//    39
//    ..
//    109
//    一位 ： 9 一个                      1          1
//    两位： 0~99 10 + 9                 20        10(1*10)
//    00 ~ 89:9 90~99:11                           位数，乘数，10
//    三位： 0~999 20*9+ 120       180+120:300     100(20*10)
//     000 ~ 899 : ,900 ~ 999：100 ， 00 ~99：19
//    四位：
//    0000 ~ 8999:9*300 + 9000~9999:1000+300   1000+(300*10)
//    五位：
//    00000 ~ 89999:                           10000+(4000*10)
//    六位：
//    000000 ~ 899999:                         100000+(50000*10) 600000
//   a      b      ten
//  100000+(50000*10)

//    0000009 ~ 99 10
//    0000019  *
    public static void main(String[] args) {
//        int i = 1000000;
        int wei = 7;
//        System.out.println(10);
//        System.out.println(new Test().getTotalNine(900,999));
//        System.out.println(100000+(50000*10));
        System.out.println(getNine(wei));


    }
    private static int getNine(int wei){
        int base = wei-1;
        for (int i = 0;i<wei-1;i++){
            base*=10;
        }
        return base;
    }


//    public int getExp(int num, int exp){
//
//    }
//    最暴力的。。。。
    public int getTotalNine(int start , int num){
        int count = 0;
        for (int i = start; i <= num; i++){
            count+=getNineNum(i);
        }
        return count;
    }
    public int getNineNum(int num){
        String s = String.valueOf(num);
        char[] cs = s.toCharArray();
        int count = 0;
        for (int i = 0; i < cs.length; i++){
            if (cs[i] == '9'){
                count++;
            }
        }
        return count;
    }
}
