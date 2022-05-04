package org.lee.domain;

import org.lee.util.StringUtil;

public enum MessageType {
    REGISTER("register"),
    SEND("send"),
    EXCHANGE_SEND("exchange_send"),
    REPLAY("replay"),
    PROPOSE("propose"),
    ACCEPT("accept"),
    HEART("accept"),
    ELECTION_DONE("election_done"),
    JOIN("join");
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
    public static boolean isRaftMessage(String messageType){
        return !StringUtil.isEmpty(messageType)
                && PROPOSE.getVal().equalsIgnoreCase(messageType)
                && ACCEPT.getVal().equalsIgnoreCase(messageType)
                && HEART.getVal().equalsIgnoreCase(messageType)
                && ELECTION_DONE.getVal().equalsIgnoreCase(messageType)
                && JOIN.getVal().equalsIgnoreCase(messageType)
                ;
    }
}
