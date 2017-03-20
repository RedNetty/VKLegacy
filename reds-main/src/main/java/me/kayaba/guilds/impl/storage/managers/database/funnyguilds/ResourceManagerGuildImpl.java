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

public class ResourceManagerGuildImpl extends AbstractDatabaseResourceManager<Guild> {

    public ResourceManagerGuildImpl(Storage storage) {
        super(storage, Guild.class, "guilds");
    }

    @Override
    public List<Guild> load() {
        getStorage().connect();
        final List<Guild> list = new ArrayList<>();

        try {
            PreparedStatement statement = getStorage().getPreparedStatement(PreparedStatements.GUILDS_SELECT);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                String homeCoordinates = res.getString("home");

                Location homeLocation = null;
                if (!homeCoordinates.isEmpty()) {
                    String[] homeSplit = org.apache.commons.lang.StringUtils.split(homeCoordinates, ',');
                    if (homeSplit.length == 4) {
                        String worldString = homeSplit[0];
                        World world = plugin.getServer().getWorld(worldString);

                        if (world != null) {
                            int x = Integer.parseInt(homeSplit[1]);
                            int y = Integer.parseInt(homeSplit[2]);
                            int z = Integer.parseInt(homeSplit[3]);
                            homeLocation = new Location(world, x, y, z);
                        }
                    }
                }


                Guild.LoadingWrapper<String> loadingWrapper = new GuildImpl.LoadingWrapper37MigrationImpl();
                loadingWrapper.setAllies(StringUtils.split(res.getString("allies"), ","));
                loadingWrapper.setWars(StringUtils.split(res.getString("enemies"), ","));

                Guild guild = new GuildImpl(UUID.fromString(res.getString("uuid")), loadingWrapper);

                guild.setAdded();
                guild.setId(1000);
                guild.setPoints(res.getInt("points"));
                guild.setName(res.getString("name"));
                guild.setTag(res.getString("tag"));
                guild.setLeaderName(res.getString("owner"));
                guild.setLives(res.getInt("lives"));
                guild.setTimeCreated(res.getLong("born") / 1000);
                guild.setHome(homeLocation);
                guild.setInactiveTime(NumberUtils.systemSeconds());
                guild.setSlots(Config.GUILD_SLOTS_START.getInt());


                guild.setUnchanged();
                ((MySQLStorageImpl) getStorage()).guildMap.put(guild.getName(), guild);
                list.add(guild);
            }
        } catch (SQLException e) {
            LoggerUtils.info("An error occurred while loading guilds!");
            LoggerUtils.exception(e);
        }

        return list;
    }

    @Override
    public boolean save(Guild guild) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void add(Guild guild) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public boolean remove(Guild guild) {
        throw new IllegalArgumentException("Not supported");
    }
}
