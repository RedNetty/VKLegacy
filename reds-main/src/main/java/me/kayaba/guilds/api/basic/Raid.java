package me.kayaba.guilds.api.basic;

import java.util.*;

public interface Raid {
    enum Result {
        DURING,
        TIMEOUT,
        SUCCESS,
        DESTROYED
    }


    Guild getGuildAttacker();


    Guild getGuildDefender();


    long getStartTime();


    int getKillsAttacker();


    int getKillsDefender();


    float getProgress();


    List<GPlayer> getPlayersOccupying();


    Result getResult();


    long getInactiveTime();


    void setGuildAttacker(Guild guild);


    void setGuildDefender(Guild guild);


    void addKillAttacker();


    void addKillDefender();


    void resetProgress();


    boolean isProgressFinished();


    void addProgress(float progress);


    void setResult(Result result);


    void updateInactiveTime();


    void addPlayerOccupying(GPlayer nPlayer);


    void removePlayerOccupying(GPlayer nPlayer);
}
