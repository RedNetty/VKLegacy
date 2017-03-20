package me.kayaba.guilds.api.util;

import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

public interface SignGUI {

    void open(Player player, String[] defaultText, SignGUIListener response);


    void open(Player player, SignGUIPattern signGUIPattern, SignGUIListener response);


    void destroy();


    Map<UUID, SignGUIListener> getListeners();


    Map<UUID, Location> getSignLocations();

    interface SignGUIListener {

        void onSignDone(Player player, String[] lines);
    }

    interface SignGUIPattern {

        String[] get();


        int getInputLine();
    }
}
