package me.kayaba.guilds.impl.basic;

import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.impl.util.*;

import java.util.*;

public abstract class AbstractResource extends AbstractChangeable implements Resource {
    private boolean added;
    private boolean unloaded;
    protected final UUID uuid;


    public AbstractResource(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public final UUID getUUID() {
        return uuid;
    }

    @Override
    public final boolean isAdded() {
        return added;
    }

    @Override
    public final void setAdded() {
        added = true;
    }

    @Override
    public final void setNotAdded() {
        added = false;
    }

    @Override
    public boolean isUnloaded() {
        return unloaded;
    }

    @Override
    public void unload() {
        unloaded = true;
    }
}
