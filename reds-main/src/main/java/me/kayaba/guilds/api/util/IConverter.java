package me.kayaba.guilds.api.util;

import java.util.*;

public interface IConverter<K, V> {

    Collection<V> convert(Collection<K> list);


    V convert(K k);
}
