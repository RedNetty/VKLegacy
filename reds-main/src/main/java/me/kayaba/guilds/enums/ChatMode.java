package me.kayaba.guilds.enums;

import me.kayaba.guilds.api.basic.*;

import java.util.*;

public enum ChatMode {
    NORMAL(Message.CHAT_GUILD_CHATMODE_NAMES_NORMAL),
    GUILD(Message.CHAT_GUILD_CHATMODE_NAMES_GUILD, Config.CHAT_GUILD_ENABLED),
    ALLY(Message.CHAT_GUILD_CHATMODE_NAMES_ALLY, Config.CHAT_ALLY_ENABLED);

    private final ConfigWrapper enabledConfig;
    private final MessageWrapper name;


    ChatMode(MessageWrapper name, ConfigWrapper config) {
        this.name = name;
        this.enabledConfig = config;
    }


    ChatMode(MessageWrapper name) {
        this.name = name;
        this.enabledConfig = null;
    }


    public boolean isEnabled() {
        return enabledConfig == null || enabledConfig.getBoolean();
    }


    public MessageWrapper getName() {
        return name;
    }


    public ChatMode next() {
        boolean n = false;
        for (ChatMode mode : values()) {
            if (!mode.isEnabled()) {
                continue;
            }

            if (n) {
                return mode;
            }

            n = mode == this;
        }

        return NORMAL;
    }


    public static ChatMode fromString(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    public static ChatMode[] valuesEnabled() {
        final List<ChatMode> list = new ArrayList<>();

        for (ChatMode mode : values()) {
            if (mode.isEnabled()) {
                list.add(mode);
            }
        }

        return list.toArray(new ChatMode[list.size()]);
    }
}
