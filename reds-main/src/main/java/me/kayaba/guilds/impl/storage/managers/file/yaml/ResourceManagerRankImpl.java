package me.kayaba.guilds.impl.storage.managers.file.yaml;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.util.converter.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.*;

import java.io.*;
import java.util.*;

public class ResourceManagerRankImpl extends AbstractYAMLResourceManager<GRank> {

    public ResourceManagerRankImpl(Storage storage) {
        super(storage, GRank.class, "rank/");
    }

    @Override
    public List<GRank> load() {
        final List<GRank> list = new ArrayList<>();

        for (File rankFile : getFiles()) {
            FileConfiguration configuration = loadConfiguration(rankFile);
            UUID rankUUID = UUID.fromString(trimExtension(rankFile));
            GRank rank = new GRankImpl(rankUUID);
            rank.setAdded();
            rank.setName(configuration.getString("name"));

            final List<String> permissionsStringList = configuration.getStringList("permissions");
            final List<GuildPermission> permissionsList = new ArrayList<>();
            for (String permissionString : permissionsStringList) {
                permissionsList.add(GuildPermission.valueOf(permissionString));
            }
            rank.setPermissions(permissionsList);

            Guild guild = GuildManager.getGuild(UUID.fromString(configuration.getString("guild")));

            if (guild == null) {
                LoggerUtils.error("Orphan rank (" + rankUUID.toString() + ") guild: " + configuration.getString("guild"));
                rank.unload();
                continue;
            }

            guild.addRank(rank);
            rank.setGuild(guild);

            for (String stringUUID : configuration.getStringList("members")) {
                GPlayer nPlayer = PlayerManager.getPlayer(new StringToUUIDConverterImpl().convert(stringUUID));

                if (nPlayer == null) {
                    LoggerUtils.error("Player " + stringUUID + " doesn't exist, cannot be added to rank '" + rank.getName() + "' of guild " + rank.getGuild().getName());
                    addToSaveQueue(rank);
                    continue;
                }

                nPlayer.setGuildRank(rank);
            }

            rank.setDefault(configuration.getBoolean("def"));
            rank.setClone(configuration.getBoolean("clone"));

            rank.setUnchanged();

            list.add(rank);
        }

        return list;
    }

    @Override
    public boolean save(GRank rank) {
        try {
            if (!rank.isChanged() && !isInSaveQueue(rank) || rank.isGeneric() || rank.isUnloaded()) {
                return false;
            }

            if (!rank.isAdded()) {
                add(rank);
            }


            FileConfiguration configuration = getData(rank);
            final Collection<String> permissionNamesList = new EnumToNameConverterImpl<GuildPermission>().convert(rank.getPermissions());


            if (!rank.isDefault() && !rank.getMembers().isEmpty()) {
                configuration.set("members", new ToStringConverterImpl().convert((List) new ResourceToUUIDConverterImpl<GPlayer>().convert(rank.getMembers())));
            } else {
                configuration.set("members", null);
            }

            configuration.set("guild", rank.getGuild().getUUID().toString());
            configuration.set("name", rank.getName());
            configuration.set("permissions", permissionNamesList);
            configuration.set("def", rank.isDefault());
            configuration.set("clone", rank.isClone());

            configuration.save(getFile(rank));
            rank.setUnchanged();
            return true;
        } catch (IOException e) {
            LoggerUtils.exception(e);
            return false;
        }
    }

    @Override
    public File getFile(GRank rank) {
        File file = new File(getDirectory(), rank.getUUID().toString() + ".yml");

        if (!file.exists()) {
            File nameFile = new File(getDirectory(), rank.getGuild().getName() + "." + StringUtils.replace(rank.getName(), " ", "_") + ".yml");
            nameFile.renameTo(file);
        }

        return file;
    }

    @Override
    public boolean remove(GRank rank) {
        if (!rank.isAdded()) {
            return false;
        }

        if (getFile(rank).delete()) {
            LoggerUtils.info("Deleted rank " + rank.getName() + "'s file.");
            return true;
        } else {
            LoggerUtils.error("Failed to delete rank " + rank.getName() + "'s file.");
            return false;
        }
    }
}
