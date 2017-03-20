package me.kayaba.guilds.impl.util.signgui;

import me.kayaba.guilds.api.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.concurrent.*;

public abstract class AbstractSignGui implements SignGUI {
    protected final Map<UUID, SignGUI.SignGUIListener> listeners = new ConcurrentHashMap<>();
    protected final Map<UUID, Location> signLocations = new ConcurrentHashMap<>();

    @Override
    public void destroy() {
        listeners.clear();
        signLocations.clear();
    }

    @Override
    public Map<UUID, SignGUI.SignGUIListener> getListeners() {
        return listeners;
    }

    @Override
    public Map<UUID, Location> getSignLocations() {
        return signLocations;
    }

    @Override
    public void open(Player player, SignGUIPattern signGUIPattern, SignGUIListener response) {
        open(player, signGUIPattern.get(), response);
    }
}
