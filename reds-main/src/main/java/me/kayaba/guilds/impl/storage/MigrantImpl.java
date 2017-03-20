package me.kayaba.guilds.impl.storage;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;

import java.util.*;

public class MigrantImpl implements Migrant {
    private final Storage fromStorage;
    private final Storage toStorage;


    public MigrantImpl(Storage fromStorage, Storage toStorage) {
        this.fromStorage = fromStorage;
        this.toStorage = toStorage;
    }

    @Override
    public Storage getFromStorage() {
        return fromStorage;
    }

    @Override
    public Storage getToStorage() {
        return toStorage;
    }

    @Override
    public void migrate() {
        boolean deleteInvalidTemp = Config.DELETEINVALID.getBoolean();
        Config.DELETEINVALID.set(false);
        Map<Class<? extends Resource>, Collection<? extends Resource>> dataMap = new LinkedHashMap<>();

        for (Map.Entry<Class<? extends Resource>, ResourceManager<? extends Resource>> fromResourceManagerEntry : getFromStorage().getResourceManagers().entrySet()) {
            List<? extends Resource> data = fromResourceManagerEntry.getValue().load();
            dataMap.put(fromResourceManagerEntry.getKey(), data);
        }

        for (Map.Entry<Class<? extends Resource>, Collection<? extends Resource>> entry : dataMap.entrySet()) {
            Collection<? extends Resource> data = entry.getValue();
            ResourceManager toResourceManager = getToStorage().getResourceManager(entry.getKey());

            for (Resource resource : new ArrayList<>(data)) {
                resource.setNotAdded();

                if (entry.getKey() == Guild.class && !PracticeServer.getInstance().getGuildManager().postCheck((Guild) resource)) {
                    LoggerUtils.error(resource.getUUID() + " failed postCheck");
                    data.remove(resource);
                }
            }


            toResourceManager.addToSaveQueue(data);
            LoggerUtils.info("Migrating " + data.size() + " of type " + entry.getKey().getSimpleName());
        }

        Config.DELETEINVALID.set(deleteInvalidTemp);
    }

    @Override
    public void save() {
        for (ResourceManager resourceManager : getToStorage().getResourceManagers().values()) {
            resourceManager.executeSave();
        }
    }
}
