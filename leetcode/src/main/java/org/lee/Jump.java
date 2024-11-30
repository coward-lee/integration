package org.lee;

public class Jump {

    public int jump(int[] nums) {
        int max = 0;
        int step = 0;
        int stepMax = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            max = Math.max(max, nums[i] + i);
            if (stepMax == i){
                step++;
                stepMax = max;
            }
        }
        return step;
    }

    public static void main(String[] args) {
        System.out.println(new Jump().jump(new int[]{
                2, 3, 1, 1, 4
        }));
    }
}
