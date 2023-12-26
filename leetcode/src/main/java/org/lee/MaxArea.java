package org.lee;

public class MaxArea {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxL = left, maxR = right;
        int currentMax = max(height, maxL, maxR);
        while (left < right) {
            // 左边向右移动
            int lMaxValue = max(height, left + 1, right);
            int rMaxValue = max(height, left, right - 1);
            boolean moveLeft = lMaxValue > rMaxValue;
            if (moveLeft && lMaxValue > currentMax) {
                currentMax = lMaxValue;
                left++;
            } else if (rMaxValue > currentMax) {
                currentMax = rMaxValue;
                right--;
            } else {
                int ingore = height[left] > height[right] ? right-- : left++;
            }
        }
        return currentMax;
    }

    private int max(int[] height, int maxL, int maxR) {
        return (maxR - maxL) * Math.min(height[maxL], height[maxR]);
    }
}
