package me.kayaba.guilds.impl.basic;

import me.kayaba.guilds.api.basic.*;

public class GenericRankImpl extends GRankImpl {

    public GenericRankImpl(String name) {
        super(name);
    }

    @Override
    public void setClone(boolean clone) {
        throw new IllegalArgumentException("Not allowed for generic ranks");
    }

    @Override
    public void delete() {
        throw new IllegalArgumentException("Not allowed for generic ranks");
    }

    @Override
    public void setName(String name) {
        throw new IllegalArgumentException("Not allowed for generic ranks");
    }

    @Override
    public void setId(int id) {
        throw new IllegalArgumentException("Not allowed for generic ranks");
    }

    @Override
    public boolean isClone() {
        throw new IllegalArgumentException("Not allowed for generic ranks");
    }

    @Override
    public void setGuild(Guild guild) {
        throw new IllegalArgumentException("Not allowed for generic ranks");
    }

    @Override
    public int getId() {
        throw new IllegalArgumentException("Not allowed for generic ranks");
    }

    @Override
    public Guild getGuild() {
        throw new IllegalArgumentException("Not allowed for generic ranks");
    }

    @Override
    public void setDefault(boolean def) {
        throw new IllegalArgumentException("Not allowed for generic ranks");
    }

    @Override
    public boolean isDefault() {
        throw new IllegalArgumentException("Not allowed for generic ranks");
    }
}
