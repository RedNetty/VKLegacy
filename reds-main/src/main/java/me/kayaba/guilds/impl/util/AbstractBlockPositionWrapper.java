package me.kayaba.guilds.impl.util;

import me.kayaba.guilds.api.util.*;
import org.bukkit.*;

public abstract class AbstractBlockPositionWrapper implements BlockPositionWrapper {
    private int x;
    private int y;
    private int z;


    public AbstractBlockPositionWrapper(Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }


    public AbstractBlockPositionWrapper(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public AbstractBlockPositionWrapper(Object blockPosition) {

    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void setZ(int z) {
        this.z = z;
    }
}
