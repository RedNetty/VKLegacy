package me.kayaba.guilds.impl.util.converter;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.manager.*;

public class NameToGuildConverterImpl extends AbstractConverter<String, Guild> {
    @Override
    public Guild convert(String s) {
        return GuildManager.getGuildByName(s);
    }
}
