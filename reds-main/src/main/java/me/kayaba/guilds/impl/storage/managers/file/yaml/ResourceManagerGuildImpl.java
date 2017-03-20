package me.kayaba.guilds.impl.storage.managers.file.yaml;

import com.google.common.collect.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.util.converter.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;

import java.io.*;
import java.util.*;

public class ResourceManagerGuildImpl extends AbstractYAMLResourceManager<Guild> {

    public ResourceManagerGuildImpl(Storage storage) {
        super(storage, Guild.class, "guild/");
    }

    @Override
    public List<Guild> load() {
        final List<Guild> list = new ArrayList<>();

        for (File guildFile : getFiles()) {
            FileConfiguration configuration = loadConfiguration(guildFile);
            boolean forceSave = false;

            if (configuration != null) {
                Guild.LoadingWrapper loadingWrapper = null;
                Collection alliesList = configuration.getStringList("allies");
                Collection alliesInvitationsList = configuration.getStringList("alliesinv");
                Collection warsList = configuration.getStringList("wars");
                Collection noWarInvitationsList = configuration.getStringList("nowar");
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
                    forceSave = true;
                }

                Guild guild;
                if (Config.GUILD_PLAYERPOINTS.getBoolean()) {
                    guild = new GuildIkkaImpl(UUID.fromString(trimExtension(guildFile)), loadingWrapper);
                } else {
                    guild = new GuildImpl(UUID.fromString(trimExtension(guildFile)), loadingWrapper);
                }

                guild.setAdded();
                guild.setId(configuration.getInt("id"));
                guild.setName(configuration.getString("name"));
                guild.setTag(configuration.getString("tag"));
                guild.setLeaderName(configuration.getString("leader"));
                guild.setMoney(configuration.getDouble("money"));
                guild.setPoints(configuration.getInt("points"));
                guild.setLives(configuration.getInt("lives"));
                guild.setSlots(configuration.getInt("slots"));
                guild.setBannerMeta(BannerUtils.deserialize(configuration.getString("banner")));
                guild.setTimeRest(configuration.getLong("timerest"));
                guild.setLostLiveTime(configuration.getLong("lostlive"));
                guild.setInactiveTime(configuration.getLong("activity"));
                guild.setTimeCreated(configuration.getLong("created"));
                guild.setOpenInvitation(configuration.getBoolean("openinv"));


                loadingWrapper = guild.getLoadingWrapper();
                loadingWrapper.setAllies(alliesList);
                loadingWrapper.setAllyInvitations(alliesInvitationsList);
                loadingWrapper.setWars(warsList);
                loadingWrapper.setNoWarInvitations(noWarInvitationsList);


                String homeWorldString = configuration.getString("home.world");
                if (homeWorldString == null || homeWorldString.isEmpty()) {
                    LoggerUtils.error("Found null or empty world (guild: " + guild.getName() + ")");
                    guild.unload();

                    if (Config.DELETEINVALID.getBoolean()) {
                        addToRemovalQueue(guild);
                    }

                    continue;
                }

                World homeWorld;
                try {
                    homeWorld = plugin.getServer().getWorld(UUID.fromString(homeWorldString));
                } catch (IllegalArgumentException e) {
                    homeWorld = plugin.getServer().getWorld(homeWorldString);
                }

                if (homeWorld == null) {
                    LoggerUtils.error("Found invalid world: " + homeWorldString + " (guild: " + guild.getName() + ")");
                    guild.unload();

                    if (Config.DELETEINVALID.getBoolean()) {
                        addToRemovalQueue(guild);
                    }

                    continue;
                }

                int x = configuration.getInt("home.x");
                int y = configuration.getInt("home.y");
                int z = configuration.getInt("home.z");
                float yaw = (float) configuration.getDouble("home.yaw");
                Location homeLocation = new Location(homeWorld, x, y, z);
                homeLocation.setYaw(yaw);
                guild.setHome(homeLocation);


                if (configuration.isConfigurationSection("bankloc")) {
                    World vaultWorld;
                    try {
                        vaultWorld = plugin.getServer().getWorld(UUID.fromString(configuration.getString("bankloc.world")));
                    } catch (IllegalArgumentException e) {
                        vaultWorld = plugin.getServer().getWorld(configuration.getString("bankloc.world"));
                    }

                    if (vaultWorld != null) {
                        x = configuration.getInt("bankloc.x");
                        y = configuration.getInt("bankloc.y");
                        z = configuration.getInt("bankloc.z");
                        Location vaultLocation = new Location(vaultWorld, x, y, z);
                        guild.setVaultLocation(vaultLocation);
                    }
                }

                guild.setUnchanged();


                if (guild.getSlots() <= 0) {
                    guild.setSlots(Config.GUILD_SLOTS_START.getInt());
                }


                if (configuration.isConfigurationSection("ranks") || forceSave) {
                    addToSaveQueue(guild);
                }

                list.add(guild);
            }
        }

        return list;
    }

    @Override
    public boolean save(Guild guild) {
        if (!guild.isChanged() && !isInSaveQueue(guild) || guild.isUnloaded()) {
            return false;
        }

        if (!guild.isAdded()) {
            add(guild);
        }

        FileConfiguration guildData = getData(guild);

        if (guildData == null) {
            LoggerUtils.error("Attempting to save non-existing guild. " + guild.getName());
            return false;
        }

        try {
            IConverter<Guild, UUID> resourceToUUIDConverter = new ResourceToUUIDConverterImpl<>();
            IConverter<Object, String> toStringConverter = new ToStringConverterImpl();


            guildData.set("name", guild.getName());
            guildData.set("tag", guild.getTag());
            guildData.set("leader", guild.getLeader().getUUID().toString());
            guildData.set("allies", toStringConverter.convert((List) resourceToUUIDConverter.convert(guild.getAllies())));
            guildData.set("alliesinv", toStringConverter.convert((List) resourceToUUIDConverter.convert(guild.getAllyInvitations())));
            guildData.set("wars", toStringConverter.convert((List) resourceToUUIDConverter.convert(guild.getWars())));
            guildData.set("nowar", toStringConverter.convert((List) resourceToUUIDConverter.convert(guild.getNoWarInvitations())));
            guildData.set("money", guild.getMoney());
            guildData.set("points", guild.getPoints());
            guildData.set("lives", guild.getLives());
            guildData.set("slots", guild.getSlots());
            guildData.set("banner", BannerUtils.serialize(guild.getBannerMeta()));

            guildData.set("timerest", guild.getTimeRest());
            guildData.set("lostlive", guild.getLostLiveTime());
            guildData.set("activity", guild.getInactiveTime());
            guildData.set("created", guild.getTimeCreated());
            guildData.set("openinv", guild.isOpenInvitation());


            Location home = guild.getHome();
            guildData.set("home.world", home.getWorld().getUID().toString());
            guildData.set("home.x", home.getBlockX());
            guildData.set("home.y", home.getBlockY());
            guildData.set("home.z", home.getBlockZ());
            guildData.set("home.yaw", home.getYaw());


            Location vaultLocation = guild.getVaultLocation();
            if (vaultLocation != null) {
                guildData.set("bankloc.world", vaultLocation.getWorld().getUID().toString());
                guildData.set("bankloc.x", vaultLocation.getBlockX());
                guildData.set("bankloc.y", vaultLocation.getBlockY());
                guildData.set("bankloc.z", vaultLocation.getBlockZ());
            } else {
                guildData.set("bankloc", null);
            }


            if (guildData.isConfigurationSection("ranks")) {
                guildData.set("ranks", null);
            }


            guildData.save(getFile(guild));
        } catch (IOException e) {
            LoggerUtils.exception(e);
        }

        return true;
    }

    @Override
    public boolean remove(Guild guild) {
        if (!guild.isAdded()) {
            return false;
        }

        if (getFile(guild).delete()) {
            LoggerUtils.info("Deleted guild " + guild.getName() + "'s file.");
            return true;
        } else {
            LoggerUtils.error("Failed to delete guild " + guild.getName() + "'s file.");
            return false;
        }
    }

    @Override
    public File getFile(Guild guild) {
        return new File(getDirectory(), guild.getUUID().toString() + ".yml");
    }
}
