package me.kayaba.guilds.api.util;

import org.bukkit.*;
import org.bukkit.entity.*;


public interface Title {

    void setTitle(String title);


    String getTitle();


    void setSubtitle(String subtitle);


    String getSubtitle();


    ChatColor getTitleColor();


    ChatColor getSubtitleColor();


    int getFadeInTime();


    int getFadeOutTime();


    int getStayTime();


    boolean getTicks();


    void setTitleColor(ChatColor color);


    void setSubtitleColor(ChatColor color);


    void setFadeInTime(int time);


    void setFadeOutTime(int time);


    void setStayTime(int time);


    void setTimingsToTicks();


    void setTimingsToSeconds();


    void send(Player player);


    void broadcast();


    void clearTitle(Player player);


    void resetTitle(Player player);
}
