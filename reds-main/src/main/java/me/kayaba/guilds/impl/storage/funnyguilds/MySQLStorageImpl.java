package me.kayaba.guilds.impl.storage.funnyguilds;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.exception.*;
import me.kayaba.guilds.impl.storage.managers.*;
import me.kayaba.guilds.impl.storage.managers.database.funnyguilds.*;
import me.kayaba.guilds.util.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class MySQLStorageImpl extends me.kayaba.guilds.impl.storage.MySQLStorageImpl {
    public Map<String, Guild> guildMap = new HashMap<>();


    public MySQLStorageImpl(String hostname, String port, String database, String username, String password) throws StorageConnectionFailedException {
        super(hostname, port, database, username, password);
    }

    @Override
    public void registerManagers() {
        new ResourceManagerGuildImpl(this);
        new ResourceManagerPlayerImpl(this);
        new ResourceManagerRegionImpl(this);
        new AbstractResourceManager<GRank>(this, GRank.class) {
            @Override
            public List<GRank> load() {
                return new ArrayList<>();
            }

            @Override
            public boolean save(GRank KayabaRank) {
                throw new IllegalArgumentException("Not supported");
            }

            @Override
            public void add(GRank KayabaRank) {
                throw new IllegalArgumentException("Not supported");
            }

            @Override
            public boolean remove(GRank KayabaRank) {
                throw new IllegalArgumentException("Not supported");
            }
        };
    }

    @Override
    protected void prepareStatements() {
        try {
            long nanoTime = System.nanoTime();
            LoggerUtils.info("Preparing statements...");
            preparedStatementMap.clear();
            connect();


            String guildsSelectSQL = "SELECT * FROM `" + Config.MYSQL_PREFIX.getString() + "guilds`";
            PreparedStatement guildsSelect = getConnection().prepareStatement(guildsSelectSQL);
            preparedStatementMap.put(PreparedStatements.GUILDS_SELECT, guildsSelect);


            String playerSelectSQL = "SELECT * FROM `" + Config.MYSQL_PREFIX.getString() + "users`";
            PreparedStatement playersSelect = getConnection().prepareStatement(playerSelectSQL);
            preparedStatementMap.put(PreparedStatements.PLAYERS_SELECT, playersSelect);


            String regionsSelectSQL = "SELECT * FROM `" + Config.MYSQL_PREFIX.getString() + "regions`";
            PreparedStatement regionsSelect = getConnection().prepareStatement(regionsSelectSQL);
            preparedStatementMap.put(PreparedStatements.REGIONS_SELECT, regionsSelect);


            LoggerUtils.info("Statements prepared in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - nanoTime), TimeUnit.NANOSECONDS) / 1000.0 + "s");
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }
    }

    @Override
    protected void analyze() {

    }
}
