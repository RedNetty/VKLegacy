package me.kayaba.guilds.impl.basic;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;

import java.util.*;

public abstract class AbstractTabList implements TabList {
    protected final List<String> lines = new ArrayList<>();
    private final GPlayer nPlayer;
    private final Map<VarKey, String> vars = new HashMap<>();


    public AbstractTabList(GPlayer nPlayer) {
        this.nPlayer = nPlayer;
        clear();
    }

    @Override
    public GPlayer getPlayer() {
        return nPlayer;
    }

    @Override
    public Map<VarKey, String> getVars() {
        return vars;
    }

    @Override
    public void clear() {
        lines.clear();
        lines.addAll(Config.TABLIST_SCHEME.getStringList());
        vars.clear();
    }
}
