package me.kayaba.guilds.impl.storage.managers.database.funnyguilds;

import me.kayaba.guilds.api.storage.*;

public abstract class AbstractDatabaseResourceManager<T extends Resource> extends me.kayaba.guilds.impl.storage.managers.database.AbstractDatabaseResourceManager<T> {

    protected AbstractDatabaseResourceManager(Storage storage, Class<T> clazz, String tableName) {
        super(storage, clazz, tableName);
    }

    @Override
    protected void updateUUID(T resource, int id) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    protected final void updateUUID(T resource) {
        throw new IllegalArgumentException("Not supported");
    }
}
