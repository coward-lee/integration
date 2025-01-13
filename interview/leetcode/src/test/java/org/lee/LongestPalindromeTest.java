package org.lee;

import org.junit.jupiter.api.Test;

public class LongestPalindromeTest {

    @Test
    void test(){
        String s = new LongestPalindrome().longestPalindrome("babad");
        System.out.println(s);
    }
}
