package me.kayaba.guilds.api.util;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.*;
import org.bukkit.block.*;

import java.util.*;

public interface RegionSelection {
    enum Type {
        HIGHLIGHT,
        HIGHLIGHT_RESIZE,
        CREATE,
        RESIZE,
        NONE
    }


    void send();


    void reset();


    void reset(GPlayer nPlayer);


    Location getCorner(Integer index);


    Integer getResizingCorner();


    Type getType();


    List<Block> getBlocks();


    GRegion getSelectedRegion();


    GPlayer getPlayer();


    List<GPlayer> getSpectators();


    RegionValidity getValidity();


    boolean isValid();


    boolean hasBothSelections();


    boolean isSent();


    int getWidth();


    int getLength();


    Location getCenter();


    World getWorld();


    void setCorner(Integer index, Location location);


    void setResizingCorner(Integer index);


    void setValidity(RegionValidity regionValidity);


    void addSpectator(GPlayer nPlayer);


    void removeSpectator(GPlayer nPlayer);


    Material getBorderMaterial();


    byte getBorderData();


    Material getCornerMaterial();


    byte getCornerData();
}
