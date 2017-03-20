package me.kayaba.guilds.impl.storage;

import me.kayaba.guilds.exception.*;
import me.kayaba.guilds.impl.storage.managers.file.yaml.*;

import java.io.*;

public class YamlStorageImpl extends AbstractFileStorage {

    public YamlStorageImpl(File dataDirectory) throws StorageConnectionFailedException {
        super(dataDirectory);
    }

    @Override
    public void registerManagers() {
        new ResourceManagerGuildImpl(this);
        new ResourceManagerPlayerImpl(this);
        new ResourceManagerRankImpl(this);
        new ResourceManagerRegionImpl(this);
    }
}
