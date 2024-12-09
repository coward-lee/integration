package org.lee;

public class lengthOfLastWord {


    public int lengthOfLastWord(String s) {
        int i = s.length() - 1;
        for (; i >= 0; i--) {
            if (s.charAt(i) != ' ') {
                break;
            }
        }
        int len = 0;
        for (; i >= 0; i--) {
            if (s.charAt(i) == ' ') {
                break;
            }
            len++;
        }
        return len;
    }
}
