package me.kayaba.guilds.impl.util.converter;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.manager.*;

import java.util.*;

public class UUIDOrNameToPlayerConverterImpl extends AbstractConverter<String, GPlayer> {
    @Override
    public GPlayer convert(String s) {
        try {
            UUID uuid = UUID.fromString(s);
            return PlayerManager.getPlayer(uuid);
        } catch (IllegalArgumentException e) {
            return PlayerManager.getPlayer(s);
        }
    }
}
