package me.kayaba.guilds.impl.storage.managers.database;

import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.storage.*;
import me.kayaba.guilds.impl.storage.managers.*;
import me.kayaba.guilds.util.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public abstract class AbstractDatabaseResourceManager<T extends Resource> extends AbstractResourceManager<T> {
    protected final String tableName;
    private final Collection<T> updateUUIDQueue = new HashSet<>();


    protected AbstractDatabaseResourceManager(Storage storage, Class<T> clazz, String tableName) {
        super(storage, clazz);
        this.tableName = tableName;
    }

    @Override
    protected final AbstractDatabaseStorage getStorage() {
        return (AbstractDatabaseStorage) super.getStorage();
    }

    @Override
    public int executeSave() {
        long startTime = System.nanoTime();
        int count = executeUpdateUUID();
        LoggerUtils.info("UUIDs updated in " + TimeUnit.MILLISECONDS.convert((System.nanoTime() - startTime), TimeUnit.NANOSECONDS) / 1000.0 + "s (" + count + " resources)");

        return super.executeSave();
    }


    public final String getTableName() {
        return tableName;
    }


    protected void updateUUID(T resource, int id) {
        try {
            String sql = "UPDATE `" + Config.MYSQL_PREFIX.getString() + getTableName() + "` SET `uuid`=? WHERE `id`=?";
            PreparedStatement statement = getStorage().getConnection().prepareStatement(sql);
            statement.setString(1, resource.getUUID().toString());
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LoggerUtils.exception(e);
        }
    }


    protected abstract void updateUUID(T resource);


    public void addToUpdateUUIDQueue(T t) {
        if (!isInUpdateUUIDQueue(t)) {
            updateUUIDQueue.add(t);
        }
    }


    public boolean isInUpdateUUIDQueue(T t) {
        return updateUUIDQueue.contains(t);
    }


    public int executeUpdateUUID() {
        int count = 0;

        for (T t : updateUUIDQueue) {
            updateUUID(t);
            count++;
        }

        updateUUIDQueue.clear();

        return count;
    }
}
