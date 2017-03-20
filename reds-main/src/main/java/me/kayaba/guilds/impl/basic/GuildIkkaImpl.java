package me.kayaba.guilds.impl.basic;

import me.kayaba.guilds.api.basic.*;

import java.util.*;


public class GuildIkkaImpl extends GuildImpl {
    public GuildIkkaImpl(UUID uuid) {
        super(uuid);
    }

    public GuildIkkaImpl(UUID uuid, LoadingWrapper loadingWrapper) {
        super(uuid, loadingWrapper);
    }

    @Override
    public int getPoints() {
        int points = 0;

        for (GPlayer nPlayer : getPlayers()) {
            points += nPlayer.getPoints();
        }

        return points;
    }

    @Override
    public void setPoints(int points) {

    }
}
