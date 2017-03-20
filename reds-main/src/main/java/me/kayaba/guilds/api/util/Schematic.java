package me.kayaba.guilds.api.util;

import org.bukkit.*;

public interface Schematic {

    void paste(Location location);


    short getWidth();


    short getHeight();


    short getLength();


    String getName();
}
