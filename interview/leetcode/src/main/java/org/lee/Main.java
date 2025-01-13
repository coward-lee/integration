package org.lee;

public class Main {
    public static void main(String[] args) {
        int[] ints = {1,2,3,4,5,6};
        reverse(ints);
        for (int anInt : ints) {
            System.out.println(anInt);
        }
    }
    // 1 2 3 4 5 6
    // 1 6 2 5 3 4

    public static void reverse(int[] ints){
        int temp = ints[0];
        boolean putTail = true;
        for (int i = 1; i < ints.length; i++) {
            if (putTail){
                temp = ints[i];
                ints[i] = ints[ints.length - i];
                putTail = false;
            }else{
                int t = temp;
                temp = ints[i];
                ints[i] = t;
                putTail = true;
            }
        }
    }

}
