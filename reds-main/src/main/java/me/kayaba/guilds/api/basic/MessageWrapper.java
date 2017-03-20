package me.kayaba.guilds.api.basic;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
import org.bukkit.inventory.*;

import java.util.*;

public interface MessageWrapper extends VarKeyApplicable<MessageWrapper> {
    enum Flag {

        NOPREFIX,


        TITLE,


        LIST,


        NOAFTERVARCOLOR
    }


    Set<Flag> getFlags();


    boolean hasFlag(Flag flag);


    boolean getTitle();


    String getPath();


    String getName();


    void setPath(String path);


    boolean isPrefix();


    boolean isEmpty();


    void send(CommandSender sender);


    void send(GPlayer nPlayer);


    MessageWrapper prefix(boolean prefix);


    void broadcast(Guild guild);


    void broadcast();


    void broadcast(Permission permission);


    String get();


    void set(String string);


    void set(List<String> list);


    ItemStack getItemStack();


    List<String> getList();


    ConfigurationSection getConfigurationSection();


    List<MessageWrapper> getNeighbours();


    String getParentPath();


    ChatBroadcast newChatBroadcast();


    MessageWrapper clone();
}
