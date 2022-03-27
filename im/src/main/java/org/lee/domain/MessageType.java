package org.lee.domain;

import org.lee.util.StringUtil;

public enum MessageType {
    REGISTER("register"), SEND("send"), REPLAY("replay");
    private final String val;


    MessageType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public static boolean isRegister(String messageType){
        return !StringUtil.isEmpty(messageType) && REGISTER.getVal().equalsIgnoreCase(messageType);
    }
}
