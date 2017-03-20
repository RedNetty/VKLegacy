package me.kayaba.guilds.enums;

public enum GuildPermission {

    ABANDON,
    KICK,
    EFFECT,
    INVITE,
    BANK_PAY,
    BANK_WITHDRAW,
    PVPTOGGLE,
    ALLY_CANCEL,
    ALLY_INVITE_SEND,
    ALLY_INVITE_CANCEL,
    ALLY_ACCEPT,
    BUYLIFE,
    BUYSLOT,
    OPENINVITATION,
    WAR_INVITE_SEND,
    WAR_INVITE_CANCEL,
    WAR_INVITE_ACCEPT,
    WAR_START,

    SET_NAME,
    SET_TAG,

    RANK_DELETE,
    RANK_LIST,
    RANK_SET,
    RANK_EDIT;


    public static GuildPermission fromString(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
