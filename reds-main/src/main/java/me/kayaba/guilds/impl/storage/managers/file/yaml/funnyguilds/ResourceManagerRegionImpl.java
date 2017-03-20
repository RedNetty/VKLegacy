package me.kayaba.guilds.impl.storage.managers.file.yaml.funnyguilds;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.storage.funnyguilds.*;
import me.kayaba.guilds.impl.storage.managers.file.yaml.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.*;

import java.io.*;
import java.util.*;

public class ResourceManagerRegionImpl extends AbstractYAMLResourceManager<GRegion> {

    public ResourceManagerRegionImpl(Storage storage) {
        super(storage, GRegion.class, "regions/");
    }

    @Override
    public List<GRegion> load() {
        final List<GRegion> list = new ArrayList<>();

        for (File regionFile : getFiles()) {
            FileConfiguration configuration = loadConfiguration(regionFile);
            String guildString = configuration.getString("name");
            String[] homeSplit = StringUtils.split(configuration.getString("center"), ',');
            String worldString = homeSplit[0];
            World world = plugin.getServer().getWorld(worldString);

            if (world == null) {
                LoggerUtils.info("Failed loading region for guild " + guildString + ", world does not exist.");
                continue;
            }

            int x = Integer.parseInt(homeSplit[1]);
            int y = Integer.parseInt(homeSplit[2]);
            int z = Integer.parseInt(homeSplit[3]);
            Location center = new Location(world, x, y, z);
            Guild guild = ((YamlStorageImpl) getStorage()).guildMap.get(guildString);

            if (guild == null) {
                LoggerUtils.error("There's no guild matching region " + guildString);
                continue;
            }

            GRegion region = new GRegionImpl(UUID.randomUUID());

            int size = configuration.getInt("size");
            Location corner1 = center.clone().add(size, 0, size);
            Location corner2 = center.clone().subtract(size, 0, size);

            region.setAdded();
            region.setCorner(0, corner1);
            region.setCorner(1, corner2);
            region.setWorld(center.getWorld());

            guild.addRegion(region);
            region.setUnchanged();
            list.add(region);
        }

        return list;
    }

    @Override
    public boolean save(GRegion KayabaRegion) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public boolean remove(GRegion KayabaRegion) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public File getFile(GRegion KayabaRegion) {
        throw new IllegalArgumentException("Not supported");
    }
}
