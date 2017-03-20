package me.kayaba.guilds.impl.storage;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.storage.*;

import java.util.*;

public abstract class AbstractStorage implements Storage {
    protected static final PracticeServer plugin = PracticeServer.getInstance();
    private final Map<Class<? extends Resource>, ResourceManager<? extends Resource>> resourceManagers = new LinkedHashMap<>();

    @Override
    public void save() {
        plugin.getGuildManager().save();
        plugin.getRankManager().save();
        plugin.getRegionManager().save();
        plugin.getPlayerManager().save();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Resource> ResourceManager<T> getResourceManager(Class<T> clazz) {
        return (ResourceManager<T>) resourceManagers.get(clazz);
    }

    @Override
    public Map<Class<? extends Resource>, ResourceManager<? extends Resource>> getResourceManagers() {
        return resourceManagers;
    }

    @Override
    public <T extends Resource> void registerResourceManager(Class<T> clazz, ResourceManager<T> resourceManager) {
        resourceManagers.put(clazz, resourceManager);
    }
}
