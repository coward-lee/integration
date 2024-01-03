package org.lee;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IntToRoman {
    public String intToRoman(int num) {
        Set<Integer> sets = Set.of(1, 4, 5, 9);
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "I");
        map.put(2, "II");
        map.put(3, "III");
        map.put(4, "IV");
        map.put(5, "V");
        map.put(6, "VI");
        map.put(7, "VII");
        map.put(8, "VIII");
        map.put(9, "IX");
        map.put(10, "X");
        map.put(20, "XX");
        map.put(30, "XXX");
        map.put(40, "XL");
        map.put(50, "L");
        map.put(60, "LX");
        map.put(70, "LXX");
        map.put(80, "LXXX");
        map.put(90, "XC");
        map.put(100, "C");
        map.put(200, "CC");
        map.put(300, "CCC");
        map.put(400, "CD");
        map.put(500, "D");
        map.put(600, "DC");
        map.put(700, "DCC");
        map.put(800, "DCCC");
        map.put(900, "CM");
        map.put(1000, "M");
        map.put(2000, "MM");
        map.put(3000, "MMM");
        if (map.containsKey(num)) {
            return map.get(num);
        }
        // 一定大于10 了
        int tmp = 1;
        StringBuilder sb = new StringBuilder();
        while (num >= tmp) {
            int modNum = num / tmp % 10;
            int t2 = modNum * tmp;
            sb.insert(0, map.getOrDefault(t2,""));

            tmp *= 10;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String s = new IntToRoman().intToRoman(101);
        System.out.println(s);
    }
}
