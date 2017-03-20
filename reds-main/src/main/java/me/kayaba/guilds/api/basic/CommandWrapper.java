package me.kayaba.guilds.api.basic;

import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.command.*;

import java.util.*;

public interface CommandWrapper {
    enum Flag {
        NOCONSOLE,
        CONFIRM
    }


    Permission getPermission();


    void setPermission(Permission permission);


    boolean hasPermission(CommandSender sender);


    TabCompleter getTabCompleter();


    void setTabCompleter(TabCompleter tabCompleter);


    boolean hasTabCompleter();


    void setFlags(Flag... flags);


    boolean hasFlag(Flag flag);


    Collection<Flag> getFlags();


    void setExecutor(CommandExecutor executor);


    CommandExecutor getExecutor();


    Class<? extends CommandExecutor> getExecutorClass();


    boolean isReversed();


    void execute(CommandSender sender, String[] args);


    MessageWrapper getUsageMessage();


    void setUsageMessage(MessageWrapper message);


    boolean allowedSender(CommandSender sender);


    Resource getExecutorVariable();


    void executorVariable(Resource resource);


    String getName();


    void setName(String name);


    boolean hasGenericCommand();


    String getGenericCommand();


    void setGenericCommand(String genericCommand);
}
