package me.kayaba.guilds.impl.storage.managers.database;

import com.google.common.collect.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.util.converter.*;
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
                boolean forceSave = false;
                boolean updateUUID = false;
                String homeCoordinates = res.getString("spawn");

                Location homeLocation = null;
                if (!homeCoordinates.isEmpty()) {
                    String[] homeSplit = org.apache.commons.lang.StringUtils.split(homeCoordinates, ';');
                    if (homeSplit.length == 5) {
                        String worldString = homeSplit[0];
                        World world;
                        try {
                            world = plugin.getServer().getWorld(UUID.fromString(worldString));
                        } catch (IllegalArgumentException e) {
                            world = plugin.getServer().getWorld(worldString);
                            forceSave = true;
                        }

                        if (world != null) {
                            int x = Integer.parseInt(homeSplit[1]);
                            int y = Integer.parseInt(homeSplit[2]);
                            int z = Integer.parseInt(homeSplit[3]);
                            float yaw = Float.parseFloat(homeSplit[4]);
                            homeLocation = new Location(world, x, y, z);
                            homeLocation.setYaw(yaw);
                        }
                    }
                }

                String vaultLocationString = res.getString("bankloc");
                Location vaultLocation = null;
                if (!vaultLocationString.isEmpty()) {
                    String[] vaultLocationSplit = vaultLocationString.split(";");

                    if (vaultLocationSplit.length == 5) {
                        String worldString = vaultLocationSplit[0];
                        World world;
                        try {
                            world = plugin.getServer().getWorld(UUID.fromString(worldString));
                        } catch (IllegalArgumentException e) {
                            world = plugin.getServer().getWorld(worldString);
                            forceSave = true;
                        }

                        if (world != null) {
                            int x = Integer.parseInt(vaultLocationSplit[1]);
                            int y = Integer.parseInt(vaultLocationSplit[2]);
                            int z = Integer.parseInt(vaultLocationSplit[3]);
                            vaultLocation = new Location(world, x, y, z);
                        }
                    }
                }


                if (homeLocation == null) {
                    LoggerUtils.info("Failed loading guild " + res.getString("name") + ", world does not exist");
                }


                Guild.LoadingWrapper loadingWrapper = null;
                Collection alliesList = StringUtils.semicolonToList(res.getString("allies"));
                Collection alliesInvitationsList = StringUtils.semicolonToList(res.getString("alliesinv"));
                Collection warsList = StringUtils.semicolonToList(res.getString("war"));
                Collection noWarInvitationsList = StringUtils.semicolonToList(res.getString("nowarinv"));
                Collection migrationList = alliesList.isEmpty()
                        ? alliesInvitationsList.isEmpty()
                        ? warsList.isEmpty()
                        ? noWarInvitationsList
                        : warsList
                        : alliesInvitationsList
                        : alliesList;

                if (migrationList.isEmpty() || StringUtils.isUUID((String) Iterables.getFirst(migrationList, null))) {
                    IConverter<String, UUID> converter = new StringToUUIDConverterImpl();
                    alliesList = converter.convert(alliesList);
                    alliesInvitationsList = converter.convert(alliesInvitationsList);
                    warsList = converter.convert(warsList);
                    noWarInvitationsList = converter.convert(noWarInvitationsList);
                } else {
                    loadingWrapper = new GuildImpl.LoadingWrapper37MigrationImpl();
                }

                UUID guildUUID;
                try {
                    guildUUID = UUID.fromString(res.getString("uuid"));
                } catch (IllegalArgumentException e) {
                    guildUUID = UUID.randomUUID();
                    updateUUID = true;
                }

                Guild guild;
                if (Config.GUILD_PLAYERPOINTS.getBoolean()) {
                    guild = new GuildIkkaImpl(guildUUID, loadingWrapper);
                } else {
                    guild = new GuildImpl(guildUUID, loadingWrapper);
                }

                guild.setAdded();
                guild.setId(res.getInt("id"));
                guild.setMoney(res.getDouble("money"));
                guild.setPoints(res.getInt("points"));
                guild.setName(res.getString("name"));
                guild.setTag(res.getString("tag"));
                guild.setLeaderName(res.getString("leader"));
                guild.setLives(res.getInt("lives"));
                guild.setTimeRest(res.getLong("timerest"));
                guild.setLostLiveTime(res.getLong("lostlive"));
                guild.setHome(homeLocation);
                guild.setVaultLocation(vaultLocation);
                guild.setSlots(res.getInt("slots"));
                guild.setBannerMeta(BannerUtils.deserialize(res.getString("banner")));
                guild.setInactiveTime(res.getLong("activity"));
                guild.setTimeCreated(res.getLong("created"));
                guild.setOpenInvitation(res.getBoolean("openinv"));

                loadingWrapper = guild.getLoadingWrapper();
                loadingWrapper.setAllies(alliesList);
                loadingWrapper.setAllyInvitations(alliesInvitationsList);
                loadingWrapper.setWars(warsList);
                loadingWrapper.setNoWarInvitations(noWarInvitationsList);


                guild.setUnchanged();


                if (guild.getSlots() <= 0) {
                    guild.setSlots(Config.GUILD_SLOTS_START.getInt());
                }

                if (guild.getId() == 0) {
                    LoggerUtils.info("Failed to load guild " + res.getString("name") + ". Invalid ID");
                    guild.unload();
                    continue;
                }

                if (forceSave) {
                    addToSaveQueue(guild);
                }

                if (updateUUID) {
                    addToUpdateUUIDQueue(guild);
                }

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
        if (!guild.isChanged() && !isInSaveQueue(guild) || guild.isUnloaded() || isInRemovalQueue(guild)) {
            return false;
        }

        if (!guild.isAdded()) {
            add(guild);
            return true;
        }

        getStorage().connect();

        try {
            String homeCoordinates = StringUtils.parseDBLocation(guild.getHome());
            String vaultLocationString = StringUtils.parseDBLocation(guild.getVaultLocation());
            IConverter<Guild, UUID> cvt = new ResourceToUUIDConverterImpl<>();

            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.GUILDS_UPDATE);

            preparedStatement.setString(1, guild.getTag());
            preparedStatement.setString(2, guild.getName());
            preparedStatement.setString(3, guild.getLeader().getUUID().toString());
            preparedStatement.setString(4, homeCoordinates);
            preparedStatement.setString(5, StringUtils.joinSemicolon(cvt.convert(guild.getAllies())));
            preparedStatement.setString(6, StringUtils.joinSemicolon(cvt.convert(guild.getAllyInvitations())));
            preparedStatement.setString(7, StringUtils.joinSemicolon(cvt.convert(guild.getWars())));
            preparedStatement.setString(8, StringUtils.joinSemicolon(cvt.convert(guild.getNoWarInvitations())));
            preparedStatement.setDouble(9, guild.getMoney());
            preparedStatement.setInt(10, guild.getPoints());
            preparedStatement.setInt(11, guild.getLives());
            preparedStatement.setLong(12, guild.getTimeRest());
            preparedStatement.setLong(13, guild.getLostLiveTime());
            preparedStatement.setLong(14, guild.getInactiveTime());
            preparedStatement.setString(15, vaultLocationString);
            preparedStatement.setInt(16, guild.getSlots());
            preparedStatement.setBoolean(17, guild.isOpenInvitation());
            preparedStatement.setString(18, BannerUtils.serialize(guild.getBannerMeta()));

            preparedStatement.setString(19, guild.getUUID().toString());

            preparedStatement.executeUpdate();
            guild.setUnchanged();
        } catch (SQLException e) {
            LoggerUtils.info("SQLException while saving a guild.");
            LoggerUtils.exception(e);
        }

        return true;
    }

    @Override
    public void add(Guild guild) {
        getStorage().connect();

        try {
            String homeLocationString = StringUtils.parseDBLocation(guild.getHome());
            String vaultLocationString = StringUtils.parseDBLocation(guild.getVaultLocation());
            IConverter<Guild, UUID> cvt = new ResourceToUUIDConverterImpl<>();

            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.GUILDS_INSERT);
            preparedStatement.setString(1, guild.getUUID().toString());
            preparedStatement.setString(2, guild.getTag());
            preparedStatement.setString(3, guild.getName());
            preparedStatement.setString(4, guild.getLeader().getUUID().toString());
            preparedStatement.setString(5, homeLocationString);
            preparedStatement.setString(6, StringUtils.joinSemicolon(cvt.convert(guild.getAllies())));
            preparedStatement.setString(7, StringUtils.joinSemicolon(cvt.convert(guild.getAllyInvitations())));
            preparedStatement.setString(8, StringUtils.joinSemicolon(cvt.convert(guild.getWars())));
            preparedStatement.setString(9, StringUtils.joinSemicolon(cvt.convert(guild.getNoWarInvitations())));
            preparedStatement.setDouble(10, guild.getMoney());
            preparedStatement.setInt(11, guild.getPoints());
            preparedStatement.setInt(12, guild.getLives());
            preparedStatement.setLong(13, guild.getTimeRest());
            preparedStatement.setLong(14, guild.getLostLiveTime());
            preparedStatement.setLong(15, guild.getInactiveTime());
            preparedStatement.setLong(16, guild.getTimeCreated());
            preparedStatement.setString(17, vaultLocationString);
            preparedStatement.setInt(18, guild.getSlots());
            preparedStatement.setBoolean(19, guild.isOpenInvitation());
            preparedStatement.setString(20, BannerUtils.serialize(guild.getBannerMeta()));

            preparedStatement.execute();

            guild.setId(getStorage().returnGeneratedKey(preparedStatement));
            guild.setUnchanged();
            guild.setAdded();
        } catch (SQLException e) {
            LoggerUtils.info("SQLException while adding a guild!");
            LoggerUtils.exception(e);
        }
    }

    @Override
    public boolean remove(Guild guild) {
        if (!guild.isAdded()) {
            return false;
        }

        getStorage().connect();

        try {
            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.GUILDS_DELETE);
            preparedStatement.setString(1, guild.getUUID().toString());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            LoggerUtils.info("SQLException while deleting a guild.");
            LoggerUtils.exception(e);
            return false;
        }
    }

    @Override
    protected void updateUUID(Guild resource) {
        updateUUID(resource, resource.getId());
    }
}
