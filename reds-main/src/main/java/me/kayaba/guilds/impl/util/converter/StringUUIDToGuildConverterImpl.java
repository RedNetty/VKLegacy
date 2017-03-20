package me.kayaba.guilds.impl.util.converter;

import me.kayaba.guilds.api.basic.*;

public class StringUUIDToGuildConverterImpl extends AbstractConverter<String, Guild> {
    @Override
    public Guild convert(String s) {
        return new UUIDToGuildConverterImpl().convert(new StringToUUIDConverterImpl().convert(s));
    }
}
