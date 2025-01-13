package org.lee;

import java.util.ArrayList;
import java.util.List;

public class summaryRanges {


    public List<String> summaryRanges(int[] nums) {
        if (nums.length == 0){
            return List.of();
        }
        List<String> lists = new ArrayList<>();
        int start = nums[0];
        int current = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (current + 1 == nums[i]) {
                current = nums[i];
                continue;
            }
            if (start != current) {
                lists.add(start + "->" + current);
            } else {
                lists.add(String.valueOf(start));
            }
            start = nums[i];
            current = nums[i];
        }
        if (start != current) {
            lists.add(start + "->" + current);
        } else {
            lists.add(String.valueOf(start));
        }
        return lists;
    }

    public static void main(String[] args) {

    }
}
