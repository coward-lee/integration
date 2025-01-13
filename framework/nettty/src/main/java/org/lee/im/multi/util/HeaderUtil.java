package org.lee.im.multi.util;

import org.lee.im.multi.config.ClientAction;

public class HeaderUtil {
    public static String getActionType(String content){
        return content;
    }

    public static boolean isLogin(String header){
        return ClientAction.LOGIN.name().equalsIgnoreCase(getActionType(header));
    }

}
