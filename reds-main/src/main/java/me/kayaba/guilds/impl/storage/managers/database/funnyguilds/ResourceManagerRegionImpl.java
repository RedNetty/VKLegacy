package me.kayaba.guilds.impl.storage.managers.database.funnyguilds;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.storage.funnyguilds.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;

import java.sql.*;
import java.util.*;

public class ResourceManagerRegionImpl extends AbstractDatabaseResourceManager<GRegion> {

    public ResourceManagerRegionImpl(Storage storage) {
        super(storage, GRegion.class, "regions");
    }

    @Override
    public List<GRegion> load() {
        getStorage().connect();
        final List<GRegion> list = new ArrayList<>();

        try {
            PreparedStatement statement = getStorage().getPreparedStatement(PreparedStatements.REGIONS_SELECT);

            ResultSet res = statement.executeQuery();
            while (res.next()) {
                String guildString = res.getString("name");
                String[] homeSplit = org.apache.commons.lang.StringUtils.split(res.getString("center"), ',');
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


                Guild guild = ((MySQLStorageImpl) getStorage()).guildMap.get(guildString);

                if (guild == null) {
                    LoggerUtils.error("There's no guild matching region " + guildString);
                    continue;
                }

                GRegion region = new GRegionImpl(UUID.randomUUID());

                int size = res.getInt("size");
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
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }

        return list;
    }

    @Override
    public boolean save(GRegion region) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void add(GRegion region) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public boolean remove(GRegion region) {
        throw new IllegalArgumentException("Not supported");
    }
}
