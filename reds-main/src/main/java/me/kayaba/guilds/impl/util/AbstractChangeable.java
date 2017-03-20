package me.kayaba.guilds.impl.util;

import me.kayaba.guilds.api.util.*;

public class AbstractChangeable implements Changeable {
    private boolean changed;

    @Override
    public final void setChanged() {
        changed = true;
    }

    @Override
    public final void setUnchanged() {
        changed = false;
    }

    @Override
    public final boolean isChanged() {
        return changed;
    }
}
