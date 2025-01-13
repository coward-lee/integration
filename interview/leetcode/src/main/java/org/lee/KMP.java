package org.lee;

public class KMP {


    public int strStr(String haystack, String needle) {
        if (haystack.length()< needle.length()){
            return -1;
        }
        int[] next = next(needle);

        int needleIndex = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle.charAt(needleIndex)) {
                needleIndex++;
                if (needleIndex == needle.length()) {
                    return i - needleIndex + 1;
                }
            } else {
                while (true){
                    if (needleIndex == 0){
                        break;
                    }
                    needleIndex = next[needleIndex - 1];
                    if (needle.charAt(needleIndex) == haystack.charAt(i)){
                        needleIndex++;
                        break;
                    }
                }
            }

        }
        return -1;
    }

    public int[] next(String haystack) {
        int[] next = new int[haystack.length()];
        int nextPrefixIndex = 0;
        for (int i = 1; i < haystack.length(); i++) {
            char c = haystack.charAt(i);
            if (c == haystack.charAt(nextPrefixIndex)) {
                next[i] = next[i - 1] + 1;
                nextPrefixIndex++;
            } else {
                int lastPreIndex = next[i - 1];
                while (lastPreIndex > 0 || haystack.charAt(lastPreIndex) == c) {
                    lastPreIndex = next[lastPreIndex - 1];
                }
                next[i] = next[lastPreIndex] + haystack.charAt(lastPreIndex) == c ? 1 : 0;
                nextPrefixIndex = next[i];
            }
        }
        return next;
    }

    public static void main(String[] args) {
        KMP kmp = new KMP();
        for (int sadbutsad : kmp.next("mississippi")) {
            System.out.println(sadbutsad);
        }

        System.out.println(kmp.strStr("hello"

                , "ll"));
        System.out.println(kmp.strStr("mississippi"


                , "issip"
        ));
    }
}
