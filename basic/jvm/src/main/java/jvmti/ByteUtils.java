package jvmti;

public class ByteUtils {
    public static int byte2Int(byte[] b, int start, int len){
        int sum = 0;
        int end = start+len;
        for (int i = start; i < end;i++){
            int n = ((int) b[i]) & 0xff;
            n <<= (--len) * 8;
            sum+=n;
        }
        return sum;
    }

    public static String byte2String(byte[] classByte, int offset, int len) {
        return new String(classByte, offset, len);
    }

    public static byte[] string2Bytes(String newStr) {
        return newStr.getBytes();
    }

    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i  < len; i++){
            b[len - i -1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

    public static byte[] bytesReplace(byte[] originalByte, int offset, int len, byte[] replaceBytes) {
        byte[] newBytes = new byte[originalByte.length + (replaceBytes.length - len)];
        System.arraycopy(originalByte, 0, newBytes, 0, offset);
        System.arraycopy(replaceBytes, 0, newBytes, offset, replaceBytes.length);
        System.arraycopy(originalByte, offset + len, newBytes,
                offset + replaceBytes.length, originalByte.length - offset -len);
        return newBytes;
    }
}
