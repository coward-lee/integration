package reactor;

import org.junit.jupiter.api.Test;

public class Inter {

    @Test
    void test_result() {
        System.out.println(countNum(0,2020,3));
    }

    public static void main(String[] args) {
        System.out.println( new Inter().countNum(0,2020,3));
        System.out.println( new Inter().countNum(0,9,3));
        System.out.println( new Inter().countNum(0,19,3));
        System.out.println( new Inter().countNum(0,39,3));
        System.out.println( new Inter().countNum(0,100,3));
        System.out.println( new Inter().countNum(100,200,3));
        System.out.println( new Inter().countNum(200,300,3));
        System.out.println( new Inter().countNum(300,400,3));
    }


    int countNum(int from,int to, int item) {
        int result = 0;
        for (int i = from; i <= to; i++) {
            int mid = i;
            while (mid > 10) {
                int mod = mid % 10;
                if (item == mod){
                    result++;
                }
                mid/=10;
            }
            if (item == mid){
                result++;
            }
        }
        return result;
    }

//    0-9   : 1             = 1
//    10-19   : 1
//    20-29   : 1
//    30-39   : 11
//    40-99  :  6            = 20
//    100 - 199 : 1+1+1+11+6  = 20
//    300- 399 : 100 + 20
//    400- 999 : 20 * 9  =  180+120 = 300

}
