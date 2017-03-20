package me.kayaba.guilds.api.basic;

import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;

import java.util.*;

public interface GRank extends Resource {

    int getId();


    String getName();


    List<GPlayer> getMembers();


    List<GuildPermission> getPermissions();


    Guild getGuild();


    boolean isClone();


    boolean isDefault();


    void setId(int id);


    void setName(String name);


    void setPermissions(List<GuildPermission> permissions);


    void setGuild(Guild guild);


    void setClone(boolean clone);


    void setDefault(boolean def);


    void addPermission(GuildPermission permission);


    void addPermissions(List<GuildPermission> list);


    void addMember(GPlayer nPlayer);


    void removePermission(GuildPermission permission);


    void removePermission(List<GuildPermission> list);


    void removeMember(GPlayer nPlayer);


    boolean hasPermission(GuildPermission permission);


    boolean isGeneric();


    void delete();
}
