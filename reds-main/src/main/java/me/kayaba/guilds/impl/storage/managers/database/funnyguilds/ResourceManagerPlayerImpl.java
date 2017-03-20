package me.kayaba.guilds.impl.storage.managers.database.funnyguilds;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.impl.storage.funnyguilds.*;
import me.kayaba.guilds.util.*;

import java.sql.*;
import java.util.*;

public class ResourceManagerPlayerImpl extends AbstractDatabaseResourceManager<GPlayer> {

    public ResourceManagerPlayerImpl(Storage storage) {
        super(storage, GPlayer.class, "users");
    }

    @Override
    public List<GPlayer> load() {
        getStorage().connect();
        final List<GPlayer> list = new ArrayList<>();

        try {
            ResultSet res = getStorage().getPreparedStatement(PreparedStatements.PLAYERS_SELECT).executeQuery();
            while (res.next()) {
                GPlayer nPlayer = new GPlayerImpl(UUID.fromString(res.getString("uuid")));
                nPlayer.setAdded();

                nPlayer.setName(res.getString("name"));
                nPlayer.setPoints(res.getInt("points"));
                nPlayer.setKills(res.getInt("kills"));
                nPlayer.setDeaths(res.getInt("deaths"));


                String guildString = res.getString("guild");
                if (guildString != null && !guildString.isEmpty()) {
                    Guild guild = ((MySQLStorageImpl) getStorage()).guildMap.get(guildString);

                    if (guild != null) {
                        guild.addPlayer(nPlayer);
                    }
                }

                nPlayer.setUnchanged();
                list.add(nPlayer);
            }
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }

        return list;
    }

    @Override
    public boolean save(GPlayer guild) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public void add(GPlayer guild) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public boolean remove(GPlayer guild) {
        throw new IllegalArgumentException("Not supported");
    }
}
