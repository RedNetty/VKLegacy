package me.kayaba.guilds.api.storage;

import me.kayaba.guilds.api.util.*;

import java.util.*;

public interface Resource extends Addable, Changeable {

    UUID getUUID();


    void unload();


    boolean isUnloaded();
}
