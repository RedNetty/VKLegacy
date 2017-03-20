package me.kayaba.guilds.impl.basic;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;

import java.util.*;

public class GRegionImpl extends AbstractResource implements GRegion {
    private final Location[] corners = new Location[2];
    private int id;
    private World world;
    private Guild guild;
    private Integer index;


    public GRegionImpl(UUID uuid) {
        super(uuid);
    }


    public GRegionImpl(UUID uuid, RegionSelection selection) {
        this(uuid);
        setCorner(0, selection.getCorner(0));
        setCorner(1, selection.getCorner(1));
        setWorld(selection.getCorner(0).getWorld());
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public int getId() {
        if (id <= 0) {
            throw new UnsupportedOperationException("This rank might have been loaded from FLAT and has 0 (or negative) ID");
        }

        return id;
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public Location getCorner(int index) {
        if (index != 0 && index != 1) {
            throw new IllegalArgumentException("Index can be either 0 or 1");
        }

        return corners[index];
    }

    @Override
    public int getWidth() {
        return Math.abs(getCorner(0).getBlockX() - getCorner(1).getBlockX()) + 1;
    }

    @Override
    public int getLength() {
        return Math.abs(getCorner(0).getBlockZ() - getCorner(1).getBlockZ()) + 1;
    }

    @Override
    public int getDiagonal() {
        int sumSquare = (int) (Math.pow(getWidth(), 2) + Math.pow(getLength(), 2));
        return Math.round((int) Math.sqrt(sumSquare));
    }

    @Override
    public int getSurface() {
        return getLength() * getWidth();
    }

    @Override
    public Location getCenter() {
        return RegionUtils.getCenterLocation(getCorner(0), getCorner(1));
    }

    @Override
    public Integer getIndex() {
        return index;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
        setChanged();
    }

    @Override
    public void setId(int id) {
        this.id = id;
        setChanged();
    }

    @Override
    public void setGuild(Guild guild) {
        this.guild = guild;
        setChanged();
    }

    @Override
    public void setCorner(int index, Location location) {
        if (index != 0 && index != 1) {
            throw new IllegalArgumentException("Index can be either 0 or 1");
        }

        Location corner = null;
        if (location != null) {
            corner = location.clone();
            corner.setY(0);
        }

        corners[index] = corner;
        setChanged();
    }

    @Override
    public void setIndex(Integer index) {
        this.index = index;
    }
}
