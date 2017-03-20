package me.kayaba.guilds.impl.util;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.event.*;

public abstract class AbstractListener implements Listener {
    protected final PracticeServer plugin = PracticeServer.getInstance();


    public AbstractListener() {
        ListenerManager.getLoggedPluginManager().registerEvents(this, plugin);
    }
}
