package me.kayaba.guilds.impl.storage.managers.file.yaml.funnyguilds;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.storage.funnyguilds.*;
import me.kayaba.guilds.impl.storage.managers.file.yaml.*;
import me.kayaba.guilds.impl.util.converter.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;

import java.io.*;
import java.util.*;

public class ResourceManagerGuildImpl extends AbstractYAMLResourceManager<Guild> {

    public ResourceManagerGuildImpl(Storage storage) {
        super(storage, Guild.class, "guilds/");
    }

    @Override
    public List<Guild> load() {
        final List<Guild> list = new ArrayList<>();

        for (File guildFile : getFiles()) {
            FileConfiguration configuration = loadConfiguration(guildFile);

            if (configuration != null) {
                Guild.LoadingWrapper<String> loadingWrapper = new GuildImpl.LoadingWrapperImpl<>(new AbstractConverter<String, Guild>() {
                    @Override
                    public Guild convert(String s) {
                        return ((YamlStorageImpl) getStorage()).guildMap.get(s);
                    }
                });

                List<String> alliesList = configuration.getStringList("allies");
                List<String> warsList = configuration.getStringList("enemies");

                Guild guild = new GuildImpl(UUID.fromString(configuration.getString("uuid")), loadingWrapper);

                guild.setAdded();
                guild.setName(configuration.getString("name"));
                guild.setTag(configuration.getString("tag"));
                guild.setLeaderName(configuration.getString("owner"));
                guild.setPoints(configuration.getInt("points"));
                guild.setLives(configuration.getInt("lives"));
                guild.setTimeCreated(configuration.getLong("born") / 1000);
                guild.setInactiveTime(NumberUtils.systemSeconds());
                guild.setSlots(Config.GUILD_SLOTS_START.getInt());


                loadingWrapper.setAllies(alliesList);
                loadingWrapper.setWars(warsList);


                String[] homeSplit = configuration.getString("home").split(",");
                World homeWorld = plugin.getServer().getWorld(homeSplit[0]);

                if (homeWorld == null) {
                    LoggerUtils.error("Found invalid world: " + homeSplit[0] + " (guild: " + guild.getName() + ")");
                    guild.unload();
                    continue;
                }

                for (String member : configuration.getStringList("members")) {
                    ((YamlStorageImpl) getStorage()).playerGuildMap.put(member, guild);
                }

                ((YamlStorageImpl) getStorage()).guildMap.put(guild.getName(), guild);
                Location homeLocation = new Location(homeWorld, Integer.parseInt(homeSplit[1]), Integer.parseInt(homeSplit[2]), Integer.parseInt(homeSplit[3]));
                guild.setHome(homeLocation);
                guild.setUnchanged();
                list.add(guild);
            }
        }

        return list;
    }

    @Override
    public boolean save(Guild guild) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public boolean remove(Guild guild) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public File getFile(Guild guild) {
        return new File(getDirectory(), guild.getName() + ".yml");
    }
}
