package me.kayaba.guilds.enums;

import me.kayaba.guilds.api.basic.*;

public enum TagColor {
    NEUTRAL(Config.CHAT_TAGCOLORS_NEUTRAL),
    ALLY(Config.CHAT_TAGCOLORS_ALLY),
    WAR(Config.CHAT_TAGCOLORS_WAR),
    GUILD(Config.CHAT_TAGCOLORS_GUILD);

    private final ConfigWrapper config;


    TagColor(ConfigWrapper config) {
        this.config = config;
    }


    public ConfigWrapper getConfig() {
        return config;
    }
}
