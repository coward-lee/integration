package org.lee.raft.enums;

public enum NodeAction {
    ELECTION_DONE,
    HEART_BEAT,
    QUESTION,
    PROPOSE,
    ACCEPT,
    JOIN;
}
