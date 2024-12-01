package org.lee;

public class productExceptSelf {


    public int[] productExceptSelf0(int[] nums) {
        int[] re = new int[nums.length];
        int total = 1;
        int exZero = 1;
        boolean totalIsZero = true;
        int zeroNum = 0;
        for (int num : nums) {
            total *= num;
            if (num == 0) {
                zeroNum++;
                continue;
            }
            exZero *= num;
            totalIsZero = false;
        }
        if (totalIsZero) {
            return re;
        }
        if (zeroNum >= 2) {
            return re;
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                re[i] = exZero;
                continue;
            }
            re[i] = total / nums[i];
        }
        return re;
    }

    public int[] productExceptSelf(int[] nums) {
        int[] re = new int[nums.length];
        int ware = 1;
        re[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            ware = ware * nums[i - 1];
            re[i] = ware;
        }

        ware = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            ware = ware * nums[i + 1];
            re[i] *= ware;
        }
        return re;
    }
}
