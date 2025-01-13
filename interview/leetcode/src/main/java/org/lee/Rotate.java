package org.lee;

public class Rotate {

    public void rotate(int[] nums, int k) {
        int len = nums.length;
        if (len == 1) {
            return;
        }
        reverse(nums, 0, len - 1);
        reverse(nums, 0, k % len - 1);
        reverse(nums, k % len, len - 1);
    }

    public void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int i = nums[start];
            nums[start] = nums[end];
            nums[end] = i;
            start++;
            end--;
        }
    }

    public static void main(String[] args) {
        int[] nums = {
                1, 2, 3, 4, 5, 6, 7
        };
        new Rotate().rotate(nums, 3);
        for (int num : nums) {
            System.out.println(num);
        }
    }
}
