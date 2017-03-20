package me.kayaba.guilds.impl.util.converter;

import me.kayaba.guilds.api.storage.*;

import java.util.*;

public class ResourceToUUIDConverterImpl<T extends Resource> extends AbstractConverter<T, UUID> {
    @Override
    public UUID convert(T resource) {
        return resource.getUUID();
    }
}
