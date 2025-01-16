package org.lee;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 滑动窗口
 */
public class FlowWindows {

    /**
     * 滑动窗口魔模版
     */
    void template() {
        int n = 1;
        //外层循环扩展右边界，内层循环扩展左边界
        for (int left = 0, right = 0; right < n; right++) {
            //当前考虑的元素
            while (left <= right && check()) {//区间[left,right]不符合题意
                //扩展左边界
                left++;
            }
            //区间[left,right]符合题意，统计相关信息
        }
    }

    boolean check() {
        return true;
    }

    @Test
    void lengthOfLongestSubstring() {
        System.out.println(lengthOfLongestSubstring("au"));
        System.out.println(lengthOfLongestSubstring("abcabcbb"));
        System.out.println(lengthOfLongestSubstring("pwwkew"));
        System.out.println(lengthOfLongestSubstring("aabaab!bb"));
    }

    public int lengthOfLongestSubstring(String s) {
        if (s.length() <= 1) {
            return s.length();
        }
        int n = s.length();
        Set<Character> sets = new HashSet<>();
        int maxLen = 0;

        //外层循环扩展右边界，内层循环扩展左边界
        for (int left = 0, right = 0; right < n; right++) {
            char c = s.charAt(right);
            //当前考虑的元素
            while (sets.contains(c)) {//区间[left,right]不符合题意
                sets.remove(s.charAt(left++));
            }
            //区间[left,right]符合题意，统计相关信息
            sets.add(c);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }
}
