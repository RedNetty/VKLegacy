package me.kayaba.guilds.impl.storage.managers.file.yaml.funnyguilds;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.storage.funnyguilds.*;
import me.kayaba.guilds.impl.storage.managers.file.yaml.*;
import org.bukkit.configuration.file.*;

import java.io.*;
import java.util.*;

public class ResourceManagerPlayerImpl extends AbstractYAMLResourceManager<GPlayer> {

    public ResourceManagerPlayerImpl(Storage storage) {
        super(storage, GPlayer.class, "users/");
    }

    @Override
    public List<GPlayer> load() {
        final List<GPlayer> list = new ArrayList<>();

        for (File playerFile : getFiles()) {
            if (!playerFile.isFile()) {
                continue;
            }

            FileConfiguration configuration = loadConfiguration(playerFile);

            if (configuration != null) {

                GPlayer nPlayer = new GPlayerImpl(UUID.fromString(configuration.getString("uuid")));
                nPlayer.setAdded();
                nPlayer.setName(configuration.getString("name"));


                nPlayer.setPoints(configuration.getInt("points"));
                nPlayer.setKills(configuration.getInt("kills"));
                nPlayer.setDeaths(configuration.getInt("deaths"));


                Guild guild = ((YamlStorageImpl) getStorage()).playerGuildMap.get(nPlayer.getName());

                if (guild != null) {
                    guild.addPlayer(nPlayer);
                }

                nPlayer.setUnchanged();
                list.add(nPlayer);
            }
        }

        return list;
    }

    @Override
    public boolean save(GPlayer KayabaPlayer) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public boolean remove(GPlayer KayabaPlayer) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public File getFile(GPlayer KayabaPlayer) {
        throw new IllegalArgumentException("Not supported");
    }
}
