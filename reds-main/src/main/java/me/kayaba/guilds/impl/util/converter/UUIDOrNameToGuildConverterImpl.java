package me.kayaba.guilds.impl.util.converter;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.util.*;

public class UUIDOrNameToGuildConverterImpl extends AbstractConverter<String, Guild> {
    @Override
    public Guild convert(String s) {
        IConverter<String, Guild> converter;
        if (StringUtils.isUUID(s)) {
            converter = new StringUUIDToGuildConverterImpl();
        } else {
            converter = new NameToGuildConverterImpl();
        }

        return converter.convert(s);
    }
}
