package org.lee.util;

import java.util.Scanner;

public class ScannerUtil {

    public static final String[] QUIT_CMD ={"Q","QUIT","EXIT"};

    public static String getLine(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    public static boolean quit(String content){
        for (String s : QUIT_CMD) {
            if (s.equalsIgnoreCase(content)){
                return true;
            }
        }
        return false;
    }
}
