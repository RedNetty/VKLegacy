package me.kayaba.guilds.impl.storage.managers.file.yaml;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.util.converter.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.configuration.file.*;

import java.io.*;
import java.util.*;

public class ResourceManagerPlayerImpl extends AbstractYAMLResourceManager<GPlayer> {

    public ResourceManagerPlayerImpl(Storage storage) {
        super(storage, GPlayer.class, "player/");
    }

    @Override
    public List<GPlayer> load() {
        final List<GPlayer> list = new ArrayList<>();
        final List<UUID> uuids = new ArrayList<>();

        for (File playerFile : getFiles()) {
            FileConfiguration configuration = loadConfiguration(playerFile);

            if (configuration != null) {
                UUID uuid = UUID.fromString(trimExtension(playerFile));
                GPlayer nPlayer = new GPlayerImpl(uuid);
                nPlayer.setAdded();
                nPlayer.setName(configuration.getString("name"));

                List<String> invitedToStringList = configuration.getStringList("invitedto");
                Collection<Guild> invitedToList = new UUIDOrNameToGuildConverterImpl().convert(invitedToStringList);

                if (!invitedToStringList.isEmpty() && !StringUtils.isUUID(invitedToStringList.get(0))) {
                    LoggerUtils.debug("Migrating invited list for player " + nPlayer.getUUID().toString());
                    addToSaveQueue(nPlayer);
                }

                if (invitedToStringList.size() != invitedToList.size()) {
                    LoggerUtils.debug("Invited to size differs for player " + nPlayer.getUUID().toString());
                    addToSaveQueue(nPlayer);
                }

                nPlayer.setInvitedTo(invitedToList);
                nPlayer.setPoints(configuration.getInt("points"));
                nPlayer.setKills(configuration.getInt("kills"));
                nPlayer.setDeaths(configuration.getInt("deaths"));


                if (uuids.contains(nPlayer.getUUID())) {
                    nPlayer.unload();

                    if (Config.DELETEINVALID.getBoolean()) {
                        addToRemovalQueue(nPlayer);
                        LoggerUtils.info("Removed doubled player: " + nPlayer.getName());
                    } else {
                        LoggerUtils.error("Doubled player: " + nPlayer.getName());
                    }

                    continue;
                }


                String guildString = configuration.getString("guild");
                if (configuration.contains("guild") && !guildString.isEmpty()) {
                    Guild guild;
                    try {
                        guild = GuildManager.getGuild(UUID.fromString(guildString));
                    } catch (IllegalArgumentException e) {
                        guild = GuildManager.getGuildByName(guildString);
                        addToSaveQueue(nPlayer);
                    }

                    if (guild != null) {
                        guild.addPlayer(nPlayer);
                    }
                }

                nPlayer.setUnchanged();

                list.add(nPlayer);
                uuids.add(nPlayer.getUUID());
            }
        }

        return list;
    }

    @Override
    public boolean save(GPlayer nPlayer) {
        if (!nPlayer.isChanged() && !isInSaveQueue(nPlayer) || nPlayer.isUnloaded()) {
            return false;
        }

        if (!nPlayer.isAdded()) {
            add(nPlayer);
        }

        FileConfiguration playerData = getData(nPlayer);

        if (playerData != null) {
            try {

                playerData.set("name", nPlayer.getName());
                playerData.set("guild", nPlayer.hasGuild() ? nPlayer.getGuild().getUUID().toString() : null);
                playerData.set("invitedto", new ToStringConverterImpl().convert((List) new ResourceToUUIDConverterImpl<Guild>().convert(nPlayer.getInvitedTo())));
                playerData.set("points", nPlayer.getPoints());
                playerData.set("kills", nPlayer.getKills());
                playerData.set("deaths", nPlayer.getDeaths());

                if (playerData.contains("uuid")) {
                    playerData.set("uuid", null);
                }


                playerData.save(getFile(nPlayer));
            } catch (IOException e) {
                LoggerUtils.exception(e);
            }
        } else {
            LoggerUtils.error("Attempting to save non-existing player. " + nPlayer.getName());
        }

        return true;
    }

    @Override
    public boolean remove(GPlayer nPlayer) {
        if (!nPlayer.isAdded()) {
            return false;
        }

        if (getFile(nPlayer).delete()) {
            LoggerUtils.info("Deleted player " + nPlayer.getName() + "'s file.");
            return true;
        } else {
            LoggerUtils.error("Failed to delete player " + nPlayer.getName() + "'s file.");
            return false;
        }
    }

    @Override
    public File getFile(GPlayer nPlayer) {
        return new File(getDirectory(), nPlayer.getUUID().toString() + ".yml");
    }
}
