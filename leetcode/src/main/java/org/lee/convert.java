package org.lee;

public class convert {
    public String convert(String s, int numRows) {
        if (numRows == 1 || s.length() ==1 || numRows >= s.length()){
            return s;
        }
        StringBuilder result = new StringBuilder();

        int i1 = 0;
        while (s.length() > i1) {
            result.append(s.charAt(i1));
            i1 += 2 * (numRows - 1);
        }
        for (int i = 1; i < numRows - 1; i++) {
            int j = i;
            result.append(s.charAt(j));
            while (true) {
                j += 2 * (numRows - i - 2) + 2;
                if (s.length() <= j) {
                    break;
                }
                result.append(s.charAt(j));
                j +=  2 * (i - 1) + 2;
                if (s.length() <= j) {
                    break;
                }
                result.append(s.charAt(j));
            }
        }

        int in = numRows - 1;
        while (s.length() > in) {
            result.append(s.charAt(in));
            in += 2 * (numRows - 1);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(new convert().convert("PAYPALISHIRING",3));
    }
}
