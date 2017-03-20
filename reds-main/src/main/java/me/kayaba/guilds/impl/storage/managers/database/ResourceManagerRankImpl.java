package me.kayaba.guilds.impl.storage.managers.database;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.util.converter.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.json.*;

import java.sql.*;
import java.util.*;

public class ResourceManagerRankImpl extends AbstractDatabaseResourceManager<GRank> {

    public ResourceManagerRankImpl(Storage storage) {
        super(storage, GRank.class, "ranks");
    }

    @Override
    public List<GRank> load() {
        getStorage().connect();
        final List<GRank> list = new ArrayList<>();

        try {
            PreparedStatement statement = getStorage().getPreparedStatement(PreparedStatements.RANKS_SELECT);

            ResultSet res = statement.executeQuery();
            while (res.next()) {
                boolean updateUUID = false;

                UUID rankUUID;
                try {
                    rankUUID = UUID.fromString(res.getString("uuid"));
                } catch (IllegalArgumentException e) {
                    rankUUID = UUID.randomUUID();
                    updateUUID = true;
                }

                GRank rank = new GRankImpl(rankUUID);
                rank.setId(res.getInt("id"));
                rank.setAdded();

                Guild guild;
                try {
                    guild = GuildManager.getGuild(UUID.fromString(res.getString("guild")));
                } catch (IllegalArgumentException e) {
                    guild = GuildManager.getGuildByName(res.getString("guild"));
                    addToSaveQueue(rank);
                }

                if (updateUUID) {
                    addToUpdateUUIDQueue(rank);
                }

                if (guild == null) {
                    LoggerUtils.error("Failed to find guild: " + res.getString("guild"));
                    rank.unload();

                    if (Config.DELETEINVALID.getBoolean()) {
                        addToRemovalQueue(rank);
                    }

                    continue;
                }

                rank.setName(res.getString("name"));
                rank.setGuild(guild);

                for (String permName : StringUtils.jsonToList(res.getString("permissions"))) {
                    rank.addPermission(GuildPermission.valueOf(permName));
                }

                List<String> memberStringList = StringUtils.jsonToList(res.getString("members"));
                Collection<GPlayer> memberList = new UUIDOrNameToPlayerConverterImpl().convert(memberStringList);

                if (memberList.size() != memberStringList.size()) {
                    addToSaveQueue(rank);
                }

                for (GPlayer nPlayer : memberList) {
                    nPlayer.setGuildRank(rank);
                }

                rank.setDefault(res.getBoolean("def"));
                rank.setClone(res.getBoolean("clone"));
                rank.setUnchanged();

                list.add(rank);
            }
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }

        return list;
    }

    @Override
    public boolean save(GRank rank) {
        if (!rank.isChanged() && !isInSaveQueue(rank) || rank.isUnloaded() || isInRemovalQueue(rank)) {
            return false;
        }

        if (!rank.isAdded()) {
            add(rank);
            return true;
        }

        getStorage().connect();

        final Collection<UUID> memberNamesList = new ResourceToUUIDConverterImpl<GPlayer>().convert(rank.getMembers());
        final Collection<String> permissionNamesList = new EnumToNameConverterImpl<GuildPermission>().convert(rank.getPermissions());

        try {
            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.RANKS_UPDATE);
            preparedStatement.setString(1, rank.getName());
            preparedStatement.setString(2, rank.getGuild().getUUID().toString());
            preparedStatement.setString(3, new JSONArray(permissionNamesList).toString());
            preparedStatement.setString(4, new JSONArray(memberNamesList).toString());
            preparedStatement.setBoolean(5, rank.isDefault());
            preparedStatement.setBoolean(6, rank.isClone());

            preparedStatement.setString(7, rank.getUUID().toString());
            preparedStatement.execute();

            rank.setUnchanged();
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }

        return true;
    }

    @Override
    public void add(GRank rank) {
        getStorage().connect();

        try {
            Collection<UUID> memberNamesList = new ResourceToUUIDConverterImpl<GPlayer>().convert(rank.getMembers());
            Collection<String> permissionNamesList = new EnumToNameConverterImpl<GuildPermission>().convert(rank.getPermissions());

            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.RANKS_INSERT);
            preparedStatement.setString(1, rank.getUUID().toString());
            preparedStatement.setString(2, rank.getName());
            preparedStatement.setString(3, rank.getGuild().getUUID().toString());
            preparedStatement.setString(4, new JSONArray(permissionNamesList).toString());
            preparedStatement.setString(5, new JSONArray(memberNamesList).toString());
            preparedStatement.setBoolean(6, rank.isDefault());
            preparedStatement.setBoolean(7, rank.isClone());
            preparedStatement.execute();

            rank.setId(getStorage().returnGeneratedKey(preparedStatement));
            rank.setUnchanged();
            rank.setAdded();
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }
    }

    @Override
    public boolean remove(GRank rank) {
        if (!rank.isAdded()) {
            return false;
        }

        getStorage().connect();

        try {
            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.RANKS_DELETE);
            preparedStatement.setString(1, rank.getUUID().toString());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            LoggerUtils.exception(e);
            return false;
        }
    }

    @Override
    protected void updateUUID(GRank resource) {
        updateUUID(resource, resource.getId());
    }
}
