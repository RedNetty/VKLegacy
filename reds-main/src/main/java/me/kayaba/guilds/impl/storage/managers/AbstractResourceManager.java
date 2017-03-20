package me.kayaba.guilds.impl.storage.managers;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.storage.*;

import java.util.*;

public abstract class AbstractResourceManager<T extends Resource> implements ResourceManager<T> {
    protected final PracticeServer plugin = PracticeServer.getInstance();
    private final Class<T> clazz;
    private final Storage storage;
    private final Collection<T> removalQueue = new HashSet<>();
    private final Collection<T> saveQueue = new HashSet<>();


    protected AbstractResourceManager(Storage storage, Class<T> clazz) {
        this.storage = storage;
        this.clazz = clazz;
        register(clazz);
    }

    @Override
    public Class<T> getClazz() {
        return clazz;
    }

    @Override
    public Integer save(Collection<T> list) {
        int count = 0;

        for (T t : list) {
            if (save(t)) {
                count++;
            }
        }

        return count;
    }

    @Override
    public int remove(Collection<T> list) {
        int count = 0;

        for (T t : list) {
            if (remove(t)) {
                count++;
            }
        }

        return count;
    }


    protected Storage getStorage() {
        return storage;
    }


    private void register(Class clazz) {
        getStorage().registerResourceManager(clazz, this);
    }

    @Override
    public int executeRemoval() {
        int count = remove(removalQueue);
        removalQueue.clear();
        return count;
    }

    @Override
    public void addToSaveQueue(T t) {
        saveQueue.add(t);
    }

    @Override
    public void addToSaveQueue(Collection<T> list) {
        for (T t : list) {
            addToSaveQueue(t);
        }
    }

    @Override
    public void removeFromSaveQueue(T t) {
        if (isInSaveQueue(t)) {
            saveQueue.remove(t);
        }
    }

    @Override
    public boolean isInSaveQueue(T t) {
        return saveQueue.contains(t);
    }

    @Override
    public void addToRemovalQueue(T t) {
        removalQueue.add(t);
    }

    @Override
    public boolean isInRemovalQueue(T t) {
        return removalQueue.contains(t);
    }

    @Override
    public int executeSave() {
        int count = save(saveQueue);
        saveQueue.clear();
        return count;
    }
}
