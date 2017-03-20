package me.kayaba.guilds.impl.util.converter;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.util.*;

import java.util.*;

public abstract class AbstractConverter<K, V> implements IConverter<K, V> {
    @Override
    public Collection<V> convert(Collection<K> list) {
        final Collection<V> convertedList = new ArrayList<>();

        for (K k : list) {
            V converted = convert(k);

            if (converted == null) {
                LoggerUtils.debug("[" + getClass().getSimpleName() + "] Converted item: " + k.toString() + " is null", false);
                continue;
            }

            convertedList.add(converted);
        }

        return convertedList;
    }
}
