package org.lee;

public class longestCommonPrefix {


    public String longestCommonPrefix(String[] strs) {
        StringBuilder builder = new StringBuilder();
        int len = strs[0].length();
        for (int i = 0; i < len; i++) {
            int j = 0;
            char c = strs[j].charAt(i);
            j++;
            for (; j < strs.length; j++) {
                if (strs[j].length() <= i) {
                    return builder.toString();
                }
                if (c != strs[j].charAt(i)) {
                    return builder.toString();
                }
            }
            if (j == strs.length) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        String[] strings = new String[]{
                "ab", "a"
        };
        System.out.println(new longestCommonPrefix().longestCommonPrefix(strings));
    }
}
