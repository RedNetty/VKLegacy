package io.vawke.practice.factory;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.HashMap;

/**
 * Created by Giovanni on 12-2-2017.
 */
public class Factory<F> {

    @Getter
    private HashMap<Class, F> factoryMap;

    public Factory() {
        this.factoryMap = Maps.newHashMap();
    }

    public boolean canRegister(F object) {
        if (object instanceof FactoryObject) {
            if (!this.factoryMap.containsKey(object.getClass())) {
                return true;
            }
        }
        return false;
    }

    public void register(F object) {
        if (this.canRegister(object)) {
            this.factoryMap.put(object.getClass(), object);
        }
    }

    public void build() {
        this.factoryMap.values().stream().filter(values -> values instanceof FactoryObject).forEach(values -> {
            ((FactoryObject) values).registerWatcher();
        });
    }
}
