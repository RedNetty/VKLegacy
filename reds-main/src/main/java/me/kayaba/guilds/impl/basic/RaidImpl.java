package me.kayaba.guilds.impl.basic;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.util.*;

import java.util.*;

public class RaidImpl implements Raid {
    private Guild guildAttacker;
    private Guild guildDefender;
    private final long startTime = NumberUtils.systemSeconds();
    private long inactiveTime = NumberUtils.systemSeconds();
    private int killsAttacker;
    private int killsDefender;
    private float progress;
    private final List<GPlayer> playersOccupying = new ArrayList<>();
    private Result result = Result.DURING;


    public RaidImpl(Guild guildAttacker, Guild guildDefender) {
        this.guildAttacker = guildAttacker;
        this.guildDefender = guildDefender;
    }

    @Override
    public Guild getGuildAttacker() {
        return guildAttacker;
    }

    @Override
    public Guild getGuildDefender() {
        return guildDefender;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public int getKillsAttacker() {
        return killsAttacker;
    }

    @Override
    public int getKillsDefender() {
        return killsDefender;
    }

    @Override
    public float getProgress() {
        return progress;
    }

    @Override
    public List<GPlayer> getPlayersOccupying() {
        return playersOccupying;
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public long getInactiveTime() {
        return inactiveTime;
    }

    @Override
    public void setGuildAttacker(Guild guild) {
        guildAttacker = guild;
    }

    @Override
    public void setGuildDefender(Guild guild) {
        guildDefender = guild;
    }

    @Override
    public void addKillAttacker() {
        killsAttacker++;
    }

    @Override
    public void addKillDefender() {
        killsDefender++;
    }

    @Override
    public void resetProgress() {
        progress = 0;
    }

    @Override
    public boolean isProgressFinished() {
        return progress >= 100;
    }

    @Override
    public void addProgress(float progress) {
        this.progress += progress;

        if (this.progress > 100) {
            this.progress = 100;
        }
    }

    @Override
    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public void updateInactiveTime() {
        inactiveTime = NumberUtils.systemSeconds();
    }

    @Override
    public void addPlayerOccupying(GPlayer nPlayer) {
        if (!playersOccupying.contains(nPlayer)) {
            playersOccupying.add(nPlayer);
        }
    }

    @Override
    public void removePlayerOccupying(GPlayer nPlayer) {
        playersOccupying.remove(nPlayer);
    }
}
