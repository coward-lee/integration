package org.lee;

import java.util.HashMap;
import java.util.Map;

public class romanToInt {


    /**
     * 13. 罗马数字转整数
     */
    Map<String, Integer> map = Map.of(
            "I", 1,
            "V", 5,
            "X", 10,
            "L", 50,
            "C", 100,
            "D", 500,
            "M", 1000
    );
    Map<String, Integer> preMap = Map.of(
            "IV", 4,
            "IX", 9,
            "XL", 40,
            "XC", 90,
            "CD", 400,
            "CM", 900
    );
    Map<String, Integer> n = new HashMap<>();

    public int romanToInt(String s) {
        n.put("I", 1);
        n.put("V", 5);
        n.put("X", 10);
        n.put("L", 50);
        n.put("C", 100);
        n.put("D", 500);
        n.put("M", 1000);
        n.put("IV", 4);
        n.put("IX", 9);
        n.put("XL", 40);
        n.put("XC", 90);
        n.put("CD", 400);
        n.put("CM", 900);
        char[] charArray = s.toCharArray();
        int result = 0;
        for (int i = 0; i < charArray.length; ) {
            String num = num(charArray, i);
            result += n.get(num);
            i += num.length();
        }
        return result;

    }

    public int romanToInt1(String s) {

        int result = 0;
        int i = 0;
        for (; i < s.length() - 1; ) {
            String substring = s.substring(i, i + 2);
            Integer integer = preMap.get(substring);
            if (integer != null) {
                result += integer;
                i += 2;
                continue;
            }
            substring = s.substring(i, i + 1);
            integer = map.get(substring);
            result += integer;
            i += 1;
        }
        if (i == s.length()) {
            result += map.get(s.substring(i, i + 1));
        }
        return result;

    }

    /**
     * 12. 整数转罗马数字
     */
    public String intToRoman(int num) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                builder.append(symbols[i]);
                num -= values[i];
            }
        }
        return builder.toString();
    }


    public String num(char[] s, int start) {
        if (start + 1 == s.length) {
            return String.valueOf(s[start]);
        }
        String s1 = s[start] + String.valueOf(s[start + 1]);
        Integer integer = preMap.get(s1);
        if (integer != null) {
            return s1;
        }
        return String.valueOf(s[start]);
    }
}
