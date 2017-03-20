package me.kayaba.guilds.manager;

import me.kayaba.guilds.impl.util.logging.*;
import me.kayaba.guilds.listener.*;
import me.kayaba.guilds.util.*;
import org.bukkit.event.*;

public class ListenerManager {
    private PacketListener packetListener;
    private static final LoggedPluginManager loggedPluginManager = new LoggedPluginManager() {
        @Override
        protected void customHandler(Event event, Throwable e) {
            LoggerUtils.exception(e);
        }
    };


    public PacketListener getPacketListener() {
        return packetListener;
    }


    public void registerListeners() {
        new LoginListener();
        new ChatListener();
        new PvpListener();
        new DeathListener();
        new PlayerInfoListener();
        new ChestGUIListener();
        packetListener = new PacketListener();
    }


    public static LoggedPluginManager getLoggedPluginManager() {
        return loggedPluginManager;
    }
}
