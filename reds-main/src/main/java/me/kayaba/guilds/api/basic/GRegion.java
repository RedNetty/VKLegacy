package me.kayaba.guilds.api.basic;

import me.kayaba.guilds.api.storage.*;
import org.bukkit.*;

public interface GRegion extends Resource {

    World getWorld();


    int getId();


    Guild getGuild();


    Location getCorner(int index);


    int getWidth();


    int getLength();


    int getDiagonal();


    int getSurface();


    Location getCenter();


    Integer getIndex();


    void setWorld(World world);


    void setId(int id);


    void setGuild(Guild guild);


    void setCorner(int index, Location location);


    void setIndex(Integer index);
}
