package me.kayaba.guilds.impl.storage.managers.file.yaml;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;

import java.io.*;
import java.util.*;

public class ResourceManagerRegionImpl extends AbstractYAMLResourceManager<GRegion> {

    public ResourceManagerRegionImpl(Storage storage) {
        super(storage, GRegion.class, "region/");
    }

    @Override
    public List<GRegion> load() {
        final List<GRegion> list = new ArrayList<>();

        for (File regionFile : getFiles()) {
            FileConfiguration configuration = loadConfiguration(regionFile);
            String guildName = trimExtension(regionFile);
            Guild guild;
            UUID regionUUID;
            boolean forceSave = false;

            if (configuration == null || configuration.getKeys(true).isEmpty()) {
                LoggerUtils.error("Null or empty configuration for region " + trimExtension(regionFile));
                continue;
            }

            World world;
            try {
                world = plugin.getServer().getWorld(UUID.fromString(configuration.getString("world")));
            } catch (IllegalArgumentException e) {
                world = plugin.getServer().getWorld(configuration.getString("world"));
            }

            if (world == null) {
                LoggerUtils.error("Null world for region " + trimExtension(regionFile));
                continue;
            }

            try {
                String guildUUIDString = configuration.getString("guild", "");
                guild = GuildManager.getGuild(UUID.fromString(guildUUIDString));
            } catch (IllegalArgumentException e) {
                guild = GuildManager.getGuildByName(guildName);
                forceSave = true;
            }

            if (guild == null) {
                LoggerUtils.error("There's no guild matching region " + guildName);
                continue;
            }

            try {
                regionUUID = UUID.fromString(trimExtension(regionFile));
            } catch (IllegalArgumentException e) {
                regionUUID = UUID.randomUUID();
                forceSave = true;
            }

            GRegion region = new GRegionImpl(regionUUID);
            region.setAdded();

            Location corner1 = new Location(world, configuration.getInt("corner1.x"), 0, configuration.getInt("corner1.z"));
            Location corner2 = new Location(world, configuration.getInt("corner2.x"), 0, configuration.getInt("corner2.z"));

            region.setCorner(0, corner1);
            region.setCorner(1, corner2);
            region.setWorld(world);
            guild.addRegion(region);
            region.setUnchanged();

            list.add(region);

            if (forceSave) {
                addToSaveQueue(region);
            }
        }

        return list;
    }

    @Override
    public boolean save(GRegion region) {
        if (!region.isChanged() && !isInSaveQueue(region) || region.isUnloaded()) {
            return false;
        }

        if (!region.isAdded()) {
            add(region);
        }

        FileConfiguration regionData = getData(region);

        if (regionData != null) {
            try {

                regionData.set("world", region.getWorld().getUID().toString());
                regionData.set("guild", region.getGuild().getUUID().toString());


                regionData.set("corner1.x", region.getCorner(0).getBlockX());
                regionData.set("corner1.z", region.getCorner(0).getBlockZ());

                regionData.set("corner2.x", region.getCorner(1).getBlockX());
                regionData.set("corner2.z", region.getCorner(1).getBlockZ());


                regionData.save(getFile(region));
                region.setUnchanged();
            } catch (IOException e) {
                LoggerUtils.exception(e);
            }
        } else {
            LoggerUtils.error("Attempting to save non-existing region. " + region.getGuild().getName());
        }

        return true;
    }

    @Override
    public boolean remove(GRegion region) {
        if (!region.isAdded()) {
            return false;
        }

        if (getFile(region).delete()) {
            return true;
        } else {
            LoggerUtils.error("Failed to delete region " + region.getUUID() + " file.");
            return false;
        }
    }

    @Override
    public File getFile(GRegion region) {
        File file = new File(getDirectory(), region.getUUID().toString() + ".yml");

        if (!file.exists()) {
            File nameFile = new File(getDirectory(), region.getGuild().getName() + ".yml");
            if (nameFile.exists() && !nameFile.renameTo(file)) {
                LoggerUtils.error("Failed to rename file " + nameFile.getName() + " to " + file.getName());
            }
        }

        return file;
    }
}
