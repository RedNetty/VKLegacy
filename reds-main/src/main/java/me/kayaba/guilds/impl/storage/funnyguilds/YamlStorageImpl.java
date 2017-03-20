package me.kayaba.guilds.impl.storage.funnyguilds;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.exception.*;
import me.kayaba.guilds.impl.storage.managers.*;
import me.kayaba.guilds.impl.storage.managers.file.yaml.funnyguilds.*;

import java.io.*;
import java.util.*;

public class YamlStorageImpl extends me.kayaba.guilds.impl.storage.YamlStorageImpl {
    public Map<String, Guild> playerGuildMap = new HashMap<>();
    public Map<String, Guild> guildMap = new HashMap<>();


    public YamlStorageImpl(File dataDirectory) throws StorageConnectionFailedException {
        super(dataDirectory);
    }

    @Override
    public boolean setUp() {
        return getDirectory().exists();
    }

    @Override
    public void registerManagers() {
        new ResourceManagerGuildImpl(this);
        new ResourceManagerPlayerImpl(this);
        new ResourceManagerRegionImpl(this);
        new AbstractResourceManager<GRank>(this, GRank.class) {
            @Override
            public List<GRank> load() {
                return new ArrayList<>();
            }

            @Override
            public boolean save(GRank KayabaRank) {
                throw new IllegalArgumentException("Not supported");
            }

            @Override
            public void add(GRank KayabaRank) {
                throw new IllegalArgumentException("Not supported");
            }

            @Override
            public boolean remove(GRank KayabaRank) {
                throw new IllegalArgumentException("Not supported");
            }
        };
    }

    @Override
    public void save() {

    }
}
