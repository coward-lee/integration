package org.lee;

import org.junit.jupiter.api.Test;

import java.util.*;

public class Solution {

    @Test
    void test_result() {
//        List<List<Integer>> lists = kSmallestPairs(new int[]{1, 7, 11}, new int[]{2, 4, 6}, 3);
//        lists.forEach(i -> {
//            i.forEach(System.out::print);
//            System.out.println();
//        });
    }

    public int findRepeatNumber(int[] nums) {
        Set<Integer> result = new HashSet<>(nums.length / 2);
        for (int i = 0; i < nums.length; i++) {
            if (result.contains(nums[i])) {
                return nums[i];
            } else {
                result.add(nums[i]);
            }
        }
        return nums[0];
    }


    @Test
    void test_findNumberIn2DArray() {
        boolean numberIn2DArray = findNumberIn2DArray(
                new int[][]{
//                        {5},
//                        {6}
                        {4, 5},
                        {4, 6},
                        {9, 14},
                        {10, 15}
                }
//                new int[][]{
//                        {1, 3, 5, 7, 9},
//                        {2, 4, 6, 8, 10},
//                        {11, 13, 15, 17, 19},
//                        {12, 14, 16, 18, 20},
//                        {21, 22, 23, 24, 25}
//                }
                , 7);
        System.out.println(numberIn2DArray);
    }

    /**
     * 在一个 n * m 的二维数组中，每一行都按照从左到右 非递减 的顺序排序，每一列都按照从上到下 非递减 的顺序排序。请完成一个高效的函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     * <p>
     * 2,5
     * 2,8
     * 7,9
     * 7,11
     * 9,11
     *
     * @param matrix
     * @param target
     * @return
     */
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix.length == 0 ){
            return false;
        }
        if (matrix[0].length == 0){
            return false;
        }
        if (matrix.length == 1){
            for (int i = 0; i < matrix[0].length; i++) {
                if (matrix[0][i]==target){
                    return true;
                }
            }
            return false;
        }
        if (matrix[0].length == 1){
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i][0]==target){
                    return true;
                }
            }
            return false;
        }
        int x = 0, y = 0;
        while (x < matrix.length && y < matrix[0].length) {
            if (matrix[x][y] < target) {

                if ((x + 1) < matrix.length && (y + 1) < matrix[0].length) {
                    if ((target < matrix[x + 1][y] && target < matrix[x][y + 1])) {
                        return false;
                    } else if (target < matrix[x + 1][y]) {
                        y++;
                    } else if (target < matrix[x][y + 1]) {
                        x++;
                    } else if ((target - matrix[x + 1][y]) < target - matrix[x][y + 1]) {
                        x++;
                    } else {
                        y++;
                    }
                } else if ((x + 1) < matrix.length) {
                    x++;
                } else if ((y + 1) < matrix[0].length) {
                    y++;
                } else {
                    return false;
                }
            } else if (matrix[x][y] == target) {
                return true;
            } else {
                if (y + 1 == matrix[0].length) {
                    for (int i = matrix[x].length - 1; i >= 0; i--) {
                        if (matrix[x][i] < target) {
                            x++;
                            break;
                        } else if (matrix[x][i] == target) {
                            return true;
                        }
                    }
                } else if (x + 1 == matrix.length) {
                    for (int i = matrix.length - 1; i >= 0; i--) {
                        if (matrix[i][y] < target) {
                            y++;
                            break;
                        } else if (matrix[i][y] == target) {
                            return true;
                        }
                    }
                } else {
                    for (int i = matrix.length - 1; i >= 0; i--) {
                        if (matrix[i][y] < target) {
                            break;
                        } else if (matrix[i][y] == target) {
                            return true;
                        }
                    }
                    for (int i = matrix[x].length - 1; i >= 0; i--) {
                        if (matrix[x][i] < target) {
                            break;
                        } else if (matrix[x][i] == target) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }


}
