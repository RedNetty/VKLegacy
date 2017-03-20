package me.kayaba.guilds.impl.util;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;

public abstract class AbstractTitle implements Title {

    protected String title = "";
    protected ChatColor titleColor = ChatColor.WHITE;


    protected String subtitle = "";
    protected ChatColor subtitleColor = ChatColor.WHITE;


    protected int fadeInTime = -1;
    protected int stayTime = -1;
    protected int fadeOutTime = -1;
    protected boolean ticks = false;


    public AbstractTitle() {
        this("", "", -1, -1, -1);
    }


    public AbstractTitle(String title) {
        this(title, "", -1, -1, -1);
    }


    public AbstractTitle(String title, String subtitle) {
        this(title, subtitle, -1, -1, -1);
    }


    public AbstractTitle(Title title) {
        this(title.getTitle(), title.getSubtitle(), title.getFadeInTime(), title.getStayTime(), title.getFadeOutTime());
        titleColor = title.getTitleColor();
        subtitleColor = title.getSubtitleColor();
        ticks = title.getTicks();
    }


    public AbstractTitle(String title, String subtitle, int fadeInTime, int stayTime, int fadeOutTime) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeInTime = fadeInTime;
        this.stayTime = stayTime;
        this.fadeOutTime = fadeOutTime;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public ChatColor getTitleColor() {
        return titleColor;
    }

    @Override
    public ChatColor getSubtitleColor() {
        return subtitleColor;
    }

    @Override
    public int getFadeInTime() {
        return fadeInTime;
    }

    @Override
    public int getFadeOutTime() {
        return fadeOutTime;
    }

    @Override
    public int getStayTime() {
        return stayTime;
    }

    @Override
    public boolean getTicks() {
        return ticks;
    }

    @Override
    public void setTitleColor(ChatColor color) {
        titleColor = color;
    }

    @Override
    public void setSubtitleColor(ChatColor color) {
        subtitleColor = color;
    }

    @Override
    public void setFadeInTime(int time) {
        fadeInTime = time;
    }

    @Override
    public void setFadeOutTime(int time) {
        fadeOutTime = time;
    }

    @Override
    public void setStayTime(int time) {
        stayTime = time;
    }

    @Override
    public void setTimingsToTicks() {
        ticks = true;
    }

    @Override
    public void setTimingsToSeconds() {
        ticks = false;
    }

    @Override
    public void broadcast() {
        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            send(player);
        }
    }
}
