package org.lee;

public class canCompleteCircuit {


    public int canCompleteCircuit(int[] gas, int[] cost) {
        int len = cost.length;
        for (int i = 0; i < len; i++) {
            int remainGas = 0;
//            int initCost = 0;
            int j = 0;
            for (; j < len; j++) {
                remainGas += gas[(j + i) % len];
                remainGas -= cost[(j + i) % len];
                if (remainGas < 0) {
                    break;
                }
            }
            if (j == len) {
                return i;
            }
            i = i + j;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(new canCompleteCircuit().canCompleteCircuit(
                new int[]{1, 2, 3, 4, 5},
                new int[]{3, 4, 5, 1, 2}
        ));
    }
}
