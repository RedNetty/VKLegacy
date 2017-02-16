package io.vawke.practice.data;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Giovanni on 12-2-2017.
 */
public class DataRegistry<V> {

    private ConcurrentHashMap<String, V> internalMap;

    public DataRegistry() {
        this.internalMap = new ConcurrentHashMap<String, V>();
    }

    public void plus(V value) {
        if (value instanceof RegistryObject) {
            this.internalMap.put(((RegistryObject) value).getDataId(), value);
        }
    }

    public void min(V value) {
        if (value instanceof RegistryObject) {
            this.internalMap.remove(((RegistryObject) value).getDataId());
        }
    }

    public V get(String key) {
        return this.internalMap.get(key);
    }

    public boolean exists(String key) {
        return this.internalMap.containsKey(key);
    }

    public int size() {
        return this.internalMap.size();
    }

    public List<V> all() {
        List<V> list = Lists.newArrayList();
        list.addAll(this.internalMap.values());
        return list;
    }
}
