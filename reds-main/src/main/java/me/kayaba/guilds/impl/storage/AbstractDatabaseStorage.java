package me.kayaba.guilds.impl.storage;

import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.storage.managers.database.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public abstract class AbstractDatabaseStorage extends AbstractStorage implements Database {
    protected Connection connection;
    protected boolean firstConnect = true;
    protected final Map<PreparedStatements, PreparedStatement> preparedStatementMap = new HashMap<>();

    @Override
    public boolean checkConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    @Override
    public final Connection getConnection() {
        return connection;
    }

    @Override
    public boolean closeConnection() throws SQLException {
        if (connection == null) {
            return false;
        }

        connection.close();
        return true;
    }


    public abstract boolean connect();


    public abstract Integer returnGeneratedKey(Statement statement);


    public abstract boolean isStatementReturnGeneratedKeysSupported();

    @Override
    public void registerManagers() {
        new ResourceManagerGuildImpl(this);
        new ResourceManagerPlayerImpl(this);
        new ResourceManagerRankImpl(this);
        new ResourceManagerRegionImpl(this);
    }

    @Override
    public boolean setUp() {
        return connect();
    }


    protected void prepareStatements() {
        try {
            long nanoTime = System.nanoTime();
            LoggerUtils.info("Preparing statements...");
            preparedStatementMap.clear();
            connect();

            int returnKeys = isStatementReturnGeneratedKeysSupported() ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;


            String guildsInsertSQL = "INSERT INTO `" + Config.MYSQL_PREFIX.getString() + "guilds` VALUES(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement guildsInsert = getConnection().prepareStatement(guildsInsertSQL, returnKeys);
            preparedStatementMap.put(PreparedStatements.GUILDS_INSERT, guildsInsert);


            String guildsSelectSQL = "SELECT * FROM `" + Config.MYSQL_PREFIX.getString() + "guilds`";
            PreparedStatement guildsSelect = getConnection().prepareStatement(guildsSelectSQL);
            preparedStatementMap.put(PreparedStatements.GUILDS_SELECT, guildsSelect);


            String guildsDeleteSQL = "DELETE FROM `" + Config.MYSQL_PREFIX.getString() + "guilds` WHERE `uuid`=?";
            PreparedStatement guildsDelete = getConnection().prepareStatement(guildsDeleteSQL);
            preparedStatementMap.put(PreparedStatements.GUILDS_DELETE, guildsDelete);


            String guildsUpdateSQL = "UPDATE `" + Config.MYSQL_PREFIX.getString() + "guilds` SET `tag`=?, `name`=?, `leader`=?, `spawn`=?, `allies`=?, `alliesinv`=?, `war`=?, `nowarinv`=?, `money`=?, `points`=?, `lives`=?, `timerest`=?, `lostlive`=?, `activity`=?, `bankloc`=?, `slots`=?, `openinv`=?, `banner`=? WHERE `uuid`=?";
            PreparedStatement guildsUpdate = getConnection().prepareStatement(guildsUpdateSQL);
            preparedStatementMap.put(PreparedStatements.GUILDS_UPDATE, guildsUpdate);


            String playersInsertSQL = "INSERT INTO `" + Config.MYSQL_PREFIX.getString() + "players` VALUES(null,?,?,?,?,?,?,?)";
            PreparedStatement playersInsert = getConnection().prepareStatement(playersInsertSQL, returnKeys);
            preparedStatementMap.put(PreparedStatements.PLAYERS_INSERT, playersInsert);


            String playerSelectSQL = "SELECT * FROM `" + Config.MYSQL_PREFIX.getString() + "players`";
            PreparedStatement playersSelect = getConnection().prepareStatement(playerSelectSQL);
            preparedStatementMap.put(PreparedStatements.PLAYERS_SELECT, playersSelect);


            String playersUpdateSQL = "UPDATE `" + Config.MYSQL_PREFIX.getString() + "players` SET `invitedto`=?, `guild`=?, `points`=?, `kills`=?, `deaths`=? WHERE `uuid`=?";
            PreparedStatement playersUpdate = getConnection().prepareStatement(playersUpdateSQL);
            preparedStatementMap.put(PreparedStatements.PLAYERS_UPDATE, playersUpdate);


            String playersDeleteSQL = "DELETE FROM `" + Config.MYSQL_PREFIX.getString() + "players` WHERE `uuid`=?";
            PreparedStatement playersDelete = getConnection().prepareStatement(playersDeleteSQL);
            preparedStatementMap.put(PreparedStatements.PLAYERS_DELETE, playersDelete);


            String regionsInsertSQL = "INSERT INTO `" + Config.MYSQL_PREFIX.getString() + "regions` VALUES(null,?,?,?,?,?);";
            PreparedStatement regionsInsert = getConnection().prepareStatement(regionsInsertSQL, returnKeys);
            preparedStatementMap.put(PreparedStatements.REGIONS_INSERT, regionsInsert);


            String regionsSelectSQL = "SELECT * FROM `" + Config.MYSQL_PREFIX.getString() + "regions`";
            PreparedStatement regionsSelect = getConnection().prepareStatement(regionsSelectSQL);
            preparedStatementMap.put(PreparedStatements.REGIONS_SELECT, regionsSelect);


            String regionsDeleteSQL = "DELETE FROM `" + Config.MYSQL_PREFIX.getString() + "regions` WHERE `uuid`=?";
            PreparedStatement regionsDelete = getConnection().prepareStatement(regionsDeleteSQL);
            preparedStatementMap.put(PreparedStatements.REGIONS_DELETE, regionsDelete);


            String regionsUpdateSQL = "UPDATE `" + Config.MYSQL_PREFIX.getString() + "regions` SET `loc_1`=?, `loc_2`=?, `guild`=?, `world`=? WHERE `uuid`=?";
            PreparedStatement regionsUpdate = getConnection().prepareStatement(regionsUpdateSQL);
            preparedStatementMap.put(PreparedStatements.REGIONS_UPDATE, regionsUpdate);


            String ranksInsertSQL = "INSERT INTO `" + Config.MYSQL_PREFIX.getString() + "ranks` VALUES(null,?,?,?,?,?,?,?);";
            PreparedStatement ranksInsert = getConnection().prepareStatement(ranksInsertSQL, returnKeys);
            preparedStatementMap.put(PreparedStatements.RANKS_INSERT, ranksInsert);


            String ranksSelectSQL = "SELECT * FROM `" + Config.MYSQL_PREFIX.getString() + "ranks`";
            PreparedStatement ranksSelect = getConnection().prepareStatement(ranksSelectSQL);
            preparedStatementMap.put(PreparedStatements.RANKS_SELECT, ranksSelect);


            String ranksDeleteSQL = "DELETE FROM `" + Config.MYSQL_PREFIX.getString() + "ranks` WHERE `uuid`=?";
            PreparedStatement ranksDelete = getConnection().prepareStatement(ranksDeleteSQL);
            preparedStatementMap.put(PreparedStatements.RANKS_DELETE, ranksDelete);


            String ranksDeleteGuildSQL = "DELETE FROM `" + Config.MYSQL_PREFIX.getString() + "ranks` WHERE `guild`=?";
            PreparedStatement ranksDeleteGuild = getConnection().prepareStatement(ranksDeleteGuildSQL);
            preparedStatementMap.put(PreparedStatements.RANKS_DELETE_GUILD, ranksDeleteGuild);


            String ranksUpdateSQL = "UPDATE `" + Config.MYSQL_PREFIX.getString() + "ranks` SET `name`=?, `guild`=?, `permissions`=?, `members`=?, `def`=?, `clone`=? WHERE `uuid`=?";
            PreparedStatement ranksUpdate = getConnection().prepareStatement(ranksUpdateSQL);
            preparedStatementMap.put(PreparedStatements.RANKS_UPDATE, ranksUpdate);


            LoggerUtils.info("Statements prepared in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - nanoTime), TimeUnit.NANOSECONDS) / 1000.0 + "s");
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }
    }


    public PreparedStatement getPreparedStatement(PreparedStatements statement) throws SQLException {
        if (preparedStatementMap.isEmpty() || !preparedStatementMap.containsKey(statement)) {
            prepareStatements();
        }

        if (preparedStatementMap.get(statement) != null && !(this instanceof SQLiteStorageImpl) && preparedStatementMap.get(statement).isClosed()) {
            prepareStatements();
        }

        PreparedStatement preparedStatement = preparedStatementMap.get(statement);
        preparedStatement.clearParameters();

        return preparedStatement;
    }


    protected boolean checkTables() throws SQLException {
        DatabaseMetaData md = getConnection().getMetaData();
        ResultSet rs = md.getTables(null, null, Config.MYSQL_PREFIX.getString() + "%", null);
        return rs.next();
    }


    protected void setupTables() throws SQLException, IOException {
        for (String tableCode : getSqlActions()) {
            Statement statement = getConnection().createStatement();
            statement.executeUpdate(tableCode);
            LoggerUtils.info("Table added to the database!");
        }
    }


    protected void analyze() {
        try {
            LoggerUtils.info("Analyzing the database...");
            DatabaseAnalyzer analyzer = new DatabaseAnalyzerImpl(getConnection());

            for (String action : getSqlActions()) {
                if (action.contains("CREATE TABLE")) {
                    String table = StringUtils.split(action, '`')[1];
                    LoggerUtils.info("Table: " + table, false);
                    analyzer.analyze(table, action);
                }
            }

            analyzer.update();
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }


    private String[] getSqlActions() throws IOException {
        InputStream inputStream = plugin.getResource("sql/" + (plugin.getConfigManager().getDataStorageType() == DataStorageType.MYSQL ? "mysql" : "sqlite") + ".sql");
        String sqlString = IOUtils.inputStreamToString(inputStream);

        if (sqlString.isEmpty() || !sqlString.contains("--")) {
            LoggerUtils.error("Invalid SQL");
            return new String[0];
        }

        sqlString = StringUtils.replace(sqlString, "{SQLPREFIX}", Config.MYSQL_PREFIX.getString());
        return sqlString.split("--");
    }
}
