public class Main1 {
    public static void main(String[] args) {
        System.out.println(reverse("babad"));
    }

    static String reverse(String input) {
        int rStart = 0;
        int rEnd = 0;
        int len = 0;
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            for (int j = chars.length - 1; j > i; j--) {
                if (isRe(chars, i, j) && (j - i) > len) {
                    rStart = i;
                    rEnd = j;
                    len = j - i;
                }
            }
        }
        return new String(chars, rStart, rEnd-rStart+1);
    }

    static boolean isRe(char[] chars, int start, int end) {
        for (int left = start, right = end; left < right; left++, right--) {
            if (left == right) {
                return true;
            }
            if (chars[left] != chars[right]) {
                return false;
            }
        }
        return true;
    }

}

