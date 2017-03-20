package me.kayaba.guilds.api.storage;

import java.util.*;

public interface ResourceManager<T extends Resource> {

    Class<T> getClazz();


    List<T> load();


    boolean save(T t);


    Integer save(Collection<T> list);


    void add(T t);


    boolean remove(T t);


    int remove(Collection<T> list);


    void addToSaveQueue(T t);


    void addToSaveQueue(Collection<T> list);


    void removeFromSaveQueue(T t);


    boolean isInSaveQueue(T t);


    void addToRemovalQueue(T t);


    boolean isInRemovalQueue(T t);


    int executeRemoval();


    int executeSave();
}
