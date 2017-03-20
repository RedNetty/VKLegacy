package me.kayaba.guilds.impl.storage;

import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.exception.*;
import me.kayaba.guilds.util.reflect.*;

import java.io.*;
import java.sql.*;

public class SQLiteStorageImpl extends AbstractDatabaseStorage implements Database {
    private final File databaseFile;
    private Throwable failureCause;


    public SQLiteStorageImpl(File databaseFile) throws StorageConnectionFailedException {
        this.databaseFile = databaseFile;

        if (!setUp() || failureCause != null) {
            throw new StorageConnectionFailedException("Failed while connecting to SQLite database", failureCause);
        }
    }

    @Override
    public void openConnection() throws SQLException, ClassNotFoundException {
        if (checkConnection()) {
            return;
        }

        if (!databaseFile.exists()) {
            try {
                if (!plugin.getDataFolder().exists()) {
                    if (!plugin.getDataFolder().mkdirs()) {
                        throw new IOException("Failed when creating directories.");
                    }
                }

                if (!databaseFile.createNewFile()) {
                    throw new IOException("Failed when creating a new file");
                }
            } catch (IOException e) {
                failureCause = e;
            }
        }

        Reflections.getClass("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().toPath().toString() + "/" + databaseFile.getName());
    }

    @Override
    public boolean connect() {
        if (firstConnect) {
            try {
                openConnection();

                if (!checkTables()) {
                    setupTables();
                }

                analyze();

                firstConnect = false;
            } catch (SQLException | ClassNotFoundException | IOException e) {
                failureCause = e;
                return false;
            }
        }
        return true;
    }

    @Override
    public Integer returnGeneratedKey(Statement statement) {
        try {
            Statement keyStatement = connection.createStatement();
            ResultSet generatedKeys = keyStatement.executeQuery("SELECT last_insert_rowid()");
            generatedKeys.next();
            int id = generatedKeys.getInt(1);

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
        return false;
    }
}
