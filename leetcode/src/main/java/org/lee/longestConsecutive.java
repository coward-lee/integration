package org.lee;

import java.util.HashSet;

public class longestConsecutive {


    public int longestConsecutive(int[] nums) {
        HashSet<Integer> num2Seq = new HashSet<>();

        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            num2Seq.add(nums[i]);
        }
        for (int num : num2Seq) {
            if (num2Seq.contains(num-1)) {
                continue;
            }

            int seq = 1;
            while (num2Seq.contains(num+1)){
                num++;
                seq++;
            }
            max = Math.max(max,seq);
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(new longestConsecutive().longestConsecutive(new int[]{
                100, 4, 200, 1, 3, 2
        }));
    }
}
