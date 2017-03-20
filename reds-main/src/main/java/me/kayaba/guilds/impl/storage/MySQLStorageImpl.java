package me.kayaba.guilds.impl.storage;

import me.kayaba.guilds.exception.*;
import me.kayaba.guilds.util.*;
import me.kayaba.guilds.util.reflect.*;

import java.io.*;
import java.sql.*;
import java.util.concurrent.*;

public class MySQLStorageImpl extends AbstractDatabaseStorage {
    private final String username;
    private final String database;
    private final String password;
    private final String port;
    private final String hostname;
    private Throwable failureCause;


    public MySQLStorageImpl(String hostname, String port, String database, String username, String password) throws StorageConnectionFailedException {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        if (!setUp()) {
            throw new StorageConnectionFailedException("Failed while connecting to MySQL database", failureCause);
        }
    }

    @Override
    public void openConnection() throws SQLException, ClassNotFoundException {
        if (checkConnection()) {
            return;
        }

        Reflections.getClass("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?autoReconnect=true", username, password);
    }

    @Override
    public boolean connect() {
        long nanoTime = System.nanoTime();

        try {
            if (!firstConnect) {
                getConnection().isValid(1000);
                if (checkConnection()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LoggerUtils.info("MySQL reconnect is required.");
        }

        try {
            openConnection();
            LoggerUtils.info("Connected to MySQL database in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - nanoTime), TimeUnit.NANOSECONDS) / 1000.0 + "s");

            if (firstConnect) {
                if (!checkTables()) {
                    setupTables();
                }

                analyze();
                firstConnect = false;
            }

            prepareStatements();

            return true;
        } catch (SQLException | ClassNotFoundException | IOException e) {
            failureCause = e;
            return false;
        }
    }

    @Override
    public Integer returnGeneratedKey(Statement statement) {
        try {
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            int id = keys.getInt(1);

            if (id == 0) {
                throw new RuntimeException("Could not get generated keys");
            }

            return id;
        } catch (SQLException e) {
            throw new RuntimeException("Could not get generated keys", e);
        }
    }

    @Override
    public boolean isStatementReturnGeneratedKeysSupported() {
        return true;
    }
}
