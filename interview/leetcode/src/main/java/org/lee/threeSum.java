package org.lee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class threeSum {

    /**
     * 思路，排序加双指针。
     * 但是这里存在一个情况，
     * 如外层 :index
     *  双指针：left 和 right
     * @param nums
     * @return
     */

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ints = new ArrayList<>();

        Arrays.sort(nums);
        for (int i = 0; i < nums.length; ) {
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                if (nums[i] + nums[left] + nums[right] == 0) {
                    ints.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left + 1] == nums[left]) left++;
                    left++;
                    while (left < right && nums[right - 1] == nums[right]) right--;
                    right--;
                } else if (nums[i] + nums[left] + nums[right] > 0) {
                    right--;
                } else {
                    left++;
                }
            }
            int next = 1;
            while (i < nums.length - next && nums[i] == nums[i + next]) {
                next++;
            }
            i += next;

        }
        return ints;
    }


    public List<List<Integer>> threeSum1(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int len = nums.length;
        if (len < 3) return result;
        Arrays.sort(nums);
        for (int i = 0; i < len; i++) {
            if (nums[i] > 0) break;
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int R = len - 1;
            int L = i + 1;
            while (R > L) {
                int sum = nums[i] + nums[L] + nums[R];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[L], nums[R]));

                    while (L < R && nums[L] == nums[L + 1]) L++;
                    while (L < R && nums[R] == nums[R - 1]) R--;
                    L++;
                    R--;
                } else if (sum > 0) {
                    R--;
                } else L++;


            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<List<Integer>> lists = new threeSum().threeSum(new int[]{
                -1, 0, 1, 2, -1, -4
        });
//        List<List<Integer>> lists1 = new threeSum().threeSum1(new int[]{
//              -3,-2,-2,1,1,1
//        });
        System.out.println(Arrays.toString(lists.toArray()));
    }
}
