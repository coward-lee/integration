package org.lee;

public class reverseWords {

    public String reverseWords(String s) {
        StringBuilder builder = new StringBuilder();
        boolean existEmpty = true;
        boolean nonEmpty = false;
        int lastIndex = s.length() - 1;
        for (int i = s.length() - 1; i >= 0; --i) {

            String substring = s.substring(i, i + 1);
            if (substring.equalsIgnoreCase(" ")) {
                if (nonEmpty) {
                    append(builder, s, i + 1, lastIndex + 1);
                }
                existEmpty = true;
                nonEmpty = false;
            } else {
                if (existEmpty) {
                    lastIndex = i;
                }
                existEmpty = false;
                nonEmpty = true;
            }
        }
        if (!s.substring(0, 1).equalsIgnoreCase(" ")) {
            append(builder, s, 0, lastIndex + 1);
        }
        return builder.toString().strip();
    }


    public void append(StringBuilder builder, String s, int start, int end) {
        if (start == end) {
            return;
        }
        builder.append(s, start, end);
        builder.append(" ");
    }

    public static void main(String[] args) {
        System.out.println(new reverseWords().reverseWords("hello world"));
    }
}
