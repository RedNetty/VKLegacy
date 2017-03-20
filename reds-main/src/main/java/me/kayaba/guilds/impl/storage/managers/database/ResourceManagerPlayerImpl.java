package me.kayaba.guilds.impl.storage.managers.database;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.util.converter.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;

import java.sql.*;
import java.util.*;

public class ResourceManagerPlayerImpl extends AbstractDatabaseResourceManager<GPlayer> {

    public ResourceManagerPlayerImpl(Storage storage) {
        super(storage, GPlayer.class, "players");
    }

    @Override
    public List<GPlayer> load() {
        getStorage().connect();
        final List<GPlayer> list = new ArrayList<>();
        final List<UUID> uuids = new ArrayList<>();

        try {
            ResultSet res = getStorage().getPreparedStatement(PreparedStatements.PLAYERS_SELECT).executeQuery();
            while (res.next()) {
                String playerName = res.getString("name");

                UUID uuid = UUID.fromString(res.getString("uuid"));
                GPlayer nPlayer = new GPlayerImpl(uuid);
                nPlayer.setAdded();

                String invitedTo = res.getString("invitedto");
                List<String> invitedToStringList = StringUtils.semicolonToList(invitedTo);
                Collection<Guild> invitedToList = new UUIDOrNameToGuildConverterImpl().convert(invitedToStringList);

                if (!invitedToStringList.isEmpty() && !StringUtils.isUUID(invitedToStringList.get(0))) {
                    addToSaveQueue(nPlayer);
                }

                if (invitedToStringList.size() != invitedToList.size()) {
                    addToSaveQueue(nPlayer);
                }

                nPlayer.setId(res.getInt("id"));
                nPlayer.setName(playerName);
                nPlayer.setInvitedTo(invitedToList);

                nPlayer.setPoints(res.getInt("points"));
                nPlayer.setKills(res.getInt("kills"));
                nPlayer.setDeaths(res.getInt("deaths"));


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


                String guildString = res.getString("guild");
                if (!guildString.isEmpty()) {
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
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }

        return list;
    }

    @Override
    public boolean save(GPlayer nPlayer) {
        if (!nPlayer.isChanged() && !isInSaveQueue(nPlayer) || nPlayer.isUnloaded() || isInRemovalQueue(nPlayer)) {
            return false;
        }

        if (!nPlayer.isAdded()) {
            add(nPlayer);
            return true;
        }

        getStorage().connect();

        try {
            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.PLAYERS_UPDATE);

            String invitedTo = StringUtils.joinSemicolon(new ResourceToUUIDConverterImpl<Guild>().convert(nPlayer.getInvitedTo()));
            String guildUUID = nPlayer.hasGuild() ? nPlayer.getGuild().getUUID().toString() : "";


            preparedStatement.setString(1, invitedTo);
            preparedStatement.setString(2, guildUUID);
            preparedStatement.setInt(3, nPlayer.getPoints());
            preparedStatement.setInt(4, nPlayer.getKills());
            preparedStatement.setInt(5, nPlayer.getDeaths());

            preparedStatement.setString(6, nPlayer.getUUID().toString());
            preparedStatement.executeUpdate();
            nPlayer.setUnchanged();
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }

        return true;
    }

    @Override
    public void add(GPlayer nPlayer) {
        getStorage().connect();

        try {
            PreparedStatement preparedStatement = getStorage().getPreparedStatement(PreparedStatements.PLAYERS_INSERT);

            String invitedTo = StringUtils.joinSemicolon(new ResourceToUUIDConverterImpl<Guild>().convert(nPlayer.getInvitedTo()));
            String guildUUID = nPlayer.hasGuild() ? nPlayer.getGuild().getUUID().toString() : "";


            preparedStatement.setString(1, nPlayer.getUUID().toString());
            preparedStatement.setString(2, nPlayer.getName());
            preparedStatement.setString(3, guildUUID);
            preparedStatement.setString(4, invitedTo);
            preparedStatement.setInt(5, nPlayer.getPoints());
            preparedStatement.setInt(6, nPlayer.getKills());
            preparedStatement.setInt(7, nPlayer.getDeaths());
            preparedStatement.executeUpdate();

            nPlayer.setId(getStorage().returnGeneratedKey(preparedStatement));
            nPlayer.setUnchanged();
            nPlayer.setAdded();
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }
    }

    @Override
    public boolean remove(GPlayer nPlayer) {
        if (!nPlayer.isAdded()) {
            return false;
        }

        getStorage().connect();

        try {
            PreparedStatement statement = getStorage().getPreparedStatement(PreparedStatements.PLAYERS_DELETE);
            statement.setString(1, nPlayer.getUUID().toString());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            LoggerUtils.exception(e);
            return false;
        }
    }

    @Override
    protected void updateUUID(GPlayer resource) {
        updateUUID(resource, resource.getId());
    }
}
