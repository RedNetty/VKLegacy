package me.kayaba.guilds.api.util;


import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;

public interface PreparedTag {

    Guild getGuild();


    TagColor getColor();


    boolean isLeaderPrefix();


    boolean isHidden();


    void setHidden(boolean hidden);


    void setLeaderPrefix(boolean leaderPrefix);


    void setColor(TagColor color);


    void setUpFor(GPlayer nPlayer);


    void setTagColorFor(GPlayer nPlayer);


    String get();
}
