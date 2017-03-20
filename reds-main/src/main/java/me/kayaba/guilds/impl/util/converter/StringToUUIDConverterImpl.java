package me.kayaba.guilds.impl.util.converter;

import java.util.*;

public class StringToUUIDConverterImpl extends AbstractConverter<String, UUID> {
    @Override
    public UUID convert(String s) {
        return UUID.fromString(s);
    }
}
