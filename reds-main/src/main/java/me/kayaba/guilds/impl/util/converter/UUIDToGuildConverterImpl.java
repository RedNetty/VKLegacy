package me.kayaba.guilds.impl.util.converter;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.manager.*;

import java.util.*;

public class UUIDToGuildConverterImpl extends AbstractConverter<UUID, Guild> {
    @Override
    public Guild convert(UUID uuid) {
        return GuildManager.getGuild(uuid);
    }
}
