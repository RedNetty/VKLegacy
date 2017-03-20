package me.kayaba.guilds.impl.storage.managers.database;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.manager.*;
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
                boolean forceSave = false;
                boolean updateUUID = false;
                World world;


                try {
                    world = plugin.getServer().getWorld(UUID.fromString(res.getString("world")));
                } catch (IllegalArgumentException e) {
                    world = plugin.getServer().getWorld(res.getString("world"));
                    forceSave = true;
                }


                String guildString = res.getString("guild");
                Guild guild;

                try {
                    guild = GuildManager.getGuild(UUID.fromString(guildString));
                } catch (IllegalArgumentException e) {
                    guild = GuildManager.getGuildByName(guildString);
                    forceSave = true;
                }

                UUID regionUUID;
                String regionUUIDString = res.getString("uuid");


                if (regionUUIDString != null && !regionUUIDString.isEmpty()) {
                    regionUUID = UUID.fromString(regionUUIDString);
                } else {
                    regionUUID = UUID.randomUUID();
                    updateUUID = true;
                }

                GRegion region = new GRegionImpl(regionUUID);

                Location corner1 = RegionUtils.deserializeLocation2D(res.getString("loc_1"));
                Location corner2 = RegionUtils.deserializeLocation2D(res.getString("loc_2"));
                corner1.setWorld(world);
                corner2.setWorld(world);

                region.setAdded();
                region.setCorner(0, corner1);
                region.setCorner(1, corner2);
                region.setWorld(world);
                region.setId(res.getInt("id"));

                if (forceSave) {
                    addToSaveQueue(region);
                }

                if (updateUUID) {
                    addToUpdateUUIDQueue(region);
                }

                if (guild == null) {
                    LoggerUtils.error("There's no guild matching region " + guildString);

                    if (Config.DELETEINVALID.getBoolean()) {
                        addToRemovalQueue(region);
                    }

                    continue;
                }

                if (world == null) {
                    LoggerUtils.info("Failed loading region for guild " + guildString + ", world does not exist.");

                    if (Config.DELETEINVALID.getBoolean()) {
                        addToRemovalQueue(region);
                    }

                    continue;
                }

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
        if (!region.isChanged() && !isInSaveQueue(region) || region.isUnloaded() || isInRemovalQueue(region)) {
            return false;
        }

        if (!region.isAdded()) {
            add(region);
            return true;
        }

        getStorage().connect();

        try {
            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.REGIONS_UPDATE);

            String loc1 = StringUtils.parseDBLocationCoordinates2D(region.getCorner(0));
            String loc2 = StringUtils.parseDBLocationCoordinates2D(region.getCorner(1));

            preparedStatement.setString(1, loc1);
            preparedStatement.setString(2, loc2);
            preparedStatement.setString(3, region.getGuild().getUUID().toString());
            preparedStatement.setString(4, region.getWorld().getUID().toString());
            preparedStatement.setString(5, region.getUUID().toString());
            preparedStatement.executeUpdate();

            region.setUnchanged();
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }

        return true;
    }

    @Override
    public void add(GRegion region) {
        getStorage().connect();

        try {
            String loc1 = StringUtils.parseDBLocationCoordinates2D(region.getCorner(0));
            String loc2 = StringUtils.parseDBLocationCoordinates2D(region.getCorner(1));

            if (region.getWorld() == null) {
                region.setWorld(Bukkit.getWorlds().get(0));
            }

            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.REGIONS_INSERT);
            preparedStatement.setString(1, region.getUUID().toString());
            preparedStatement.setString(2, loc1);
            preparedStatement.setString(3, loc2);
            preparedStatement.setString(4, region.getGuild().getUUID().toString());
            preparedStatement.setString(5, region.getWorld().getUID().toString());
            preparedStatement.executeUpdate();

            region.setId(getStorage().returnGeneratedKey(preparedStatement));
            region.setUnchanged();
            region.setAdded();
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }
    }

    @Override
    public boolean remove(GRegion region) {
        if (!region.isAdded()) {
            return false;
        }

        getStorage().connect();

        try {
            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.REGIONS_DELETE);
            preparedStatement.setString(1, region.getUUID().toString());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            LoggerUtils.info("An error occurred while deleting a guild's region (" + region.getGuild().getName() + ")");
            LoggerUtils.exception(e);
            return false;
        }
    }

    @Override
    protected void updateUUID(GRegion resource) {
        updateUUID(resource, resource.getId());
    }
}
