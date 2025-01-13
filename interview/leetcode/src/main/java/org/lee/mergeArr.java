package org.lee;

import java.util.Arrays;

public class mergeArr {

    public int[][] merge(int[][] intervals) {

        for (int i = 0; i < intervals.length; i++) {
            for (int j = i + 1; j < intervals.length; j++) {
                if (intervals[i][0] > intervals[j][0]) {
                    int[] tempArr = intervals[j];
                    intervals[j] = intervals[i];
                    intervals[i] = tempArr;
                }
            }
        }


        int[][] result = new int[intervals.length][2];
        int mergedLength = 0;
        int[] seg = new int[2];
        System.arraycopy(intervals[0], 0, seg, 0, 2);

        for (int i = 1; i < intervals.length; i++) {
            if (seg[1] < intervals[i][0]) {
                result[mergedLength] = seg;
                mergedLength++;
                seg = new int[2];
                System.arraycopy(intervals[i], 0, seg, 0, 2);
                continue;
            }
            merge(intervals[i], seg);
        }
        result[mergedLength] = seg;
        mergedLength++;


        int[][] actualResult = new int[mergedLength][2];
        System.arraycopy(result, 0, actualResult, 0, mergedLength);
        return actualResult;
    }

    private void merge(int[] interval, int[] seg) {
        seg[0] = Math.min(seg[0], interval[0]);
        seg[1] = Math.max(seg[1], interval[1]);
    }

    public static void main(String[] args) {
        int[][] merge = new mergeArr().merge(new int[][]{
//                {1, 3}, {2, 6}, {8, 10}, {15, 18}
                {1, 4}, {0, 1}
        });
        for (int[] ints : merge) {
            System.out.println(Arrays.toString(ints));
        }
    }
}
