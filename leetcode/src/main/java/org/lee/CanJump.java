package org.lee;

public class CanJump {

    public boolean canJump(int[] nums) {
        if (nums.length == 1) {
            return true;
        }
        if (nums[0] == 0) {
            return false;
        }

        int end = nums.length - 2;
        boolean preIsZero = false;
        int jumpSkip0Step = 0;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (!preIsZero) {
                end = i;
                preIsZero = nums[i] == 0;
                jumpSkip0Step = preIsZero ? 1 : 0;
                continue;
            }

            // nums[i] > 0
            if (jumpSkip0Step < nums[i]) {
                end = i;
                preIsZero = false;
                jumpSkip0Step = 1;
                continue;
            } else {
                jumpSkip0Step++;
            }
        }
        return end == 0;
    }

    public boolean canJump2(int[] nums) {

        int maxSkipTo = nums[0];
        int i = 0;
        for (; i < nums.length; i++) {
            if (maxSkipTo <= i && nums[i] == 0) {
                break;
            }
            maxSkipTo = Math.max(nums[i] + i, maxSkipTo);
        }
        return maxSkipTo >= nums.length - 1;
    }

    public static void main(String[] args) {
        System.out.println(new CanJump().canJump2(new int[]{
                3,2,1,0,4
        }));
    }
}
