package me.kayaba.guilds.api.basic;

import me.kayaba.guilds.api.storage.*;
import org.bukkit.*;

import java.util.*;

public interface Hologram extends Resource {

    String getName();


    Location getLocation();


    List<String> getLines();


    void setName(String name);


    void setLocation(Location location);


    void addLine(String line);


    void clearLines();


    void addLine(List<String> lines);


    void refresh();


    void teleport(Location location);


    void create();


    void delete();


    boolean isTop();


    boolean isDeleted();


    void setTop(boolean top);
}
