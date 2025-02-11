package org.lee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class spiralOrder {

    /**
     * 54. 螺旋矩阵
     */
    public List<Integer> spiralOrder(int[][] matrix) {

        int top = 0, left = 0, right = matrix[0].length, bottom = matrix.length;
        List<Integer> arrs = new ArrayList<>();
        while (top <= bottom && left <= right) {
            for (int i = left; i < right; i++) {
                arrs.add(matrix[top][i]);
            }
            for (int i = top+1; i < bottom; i++) {
                arrs.add(matrix[i][right-1]);
            }
            if (left < right && top < bottom) {
                for (int i = right - 2; i > left; i--) {
                    arrs.add(matrix[bottom - 1][i]);
                }
                for (int i = bottom - 1; i > top; i--) {
                    arrs.add(matrix[i][left]);
                }
            }
            top++;
            bottom--;
            left++;
            right--;
        }
        return arrs;
    }

    public static void main(String[] args) {
        int[][] matrix = new int[3][3];
        matrix[0] = new int[]{1, 2, 3};
        matrix[1] = new int[]{4, 5, 6};
        matrix[2] = new int[]{7, 8, 9};
        List<Integer> integers = new spiralOrder().spiralOrder(
                matrix
        );
        System.out.println(Arrays.toString(integers.toArray()));
    }
}
