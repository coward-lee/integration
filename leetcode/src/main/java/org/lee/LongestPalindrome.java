package org.lee;

public class LongestPalindrome {

    public String longestPalindrome(String s) {
        char[] chars = s.toCharArray();
        char[] aux = new char[chars.length * 2 + 1];
        for (int i = 0; i < chars.length; i++) {
            aux[i * 2] = '#';
            aux[i * 2 + 1] = chars[i];
        }
        aux[chars.length * 2] = '#';
        int max = 1, index = 1;
        for (int i = 1; i < aux.length; i++) {
            int len = get(aux, i);
            if (max < len) {
                max = len;
                index = i;
            }
        }
        int start = (index - max / 2) / 2;
        int end = (index + max / 2) / 2;
        return s.substring(start, end);
    }


    private int get(char[] chars, int midIndex) {
        int left = midIndex - 1;
        int right = midIndex + 1;
        for (; right < chars.length && left >= 0; left--, right++) {
            if (chars[left] != chars[right]) {
                return right - left - 1;
            }
        }
        return right - left - 1;
    }
}
