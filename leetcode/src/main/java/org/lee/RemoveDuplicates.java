package org.lee;

public class RemoveDuplicates {

    public int removeDuplicates(int[] nums) {
        int newIndex = 1;
        for (int i = 2; i < nums.length; i++) {
            if ( nums[newIndex] != nums[i] || nums[newIndex] != nums[newIndex - 1]) {
                nums[newIndex + 1] = nums[i];
                newIndex++;
            }
        }
        return newIndex + 1;
    }

    public static void main(String[] args) {
        int[] nums = {
                1, 1, 1, 2, 2, 3
        };
        for (int i = 0; i < new RemoveDuplicates().removeDuplicates(nums); i++) {
            System.out.println(nums[i]);
        }
    }
}
