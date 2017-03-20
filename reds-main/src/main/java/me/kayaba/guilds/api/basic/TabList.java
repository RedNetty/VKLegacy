package me.kayaba.guilds.api.basic;

import me.kayaba.guilds.enums.*;

import java.util.*;

public interface TabList {

    void send();


    void clear();


    GPlayer getPlayer();


    Map<VarKey, String> getVars();
}
