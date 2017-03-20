package me.kayaba.guilds.api.basic;

import org.bukkit.command.*;

import java.util.*;

public interface CommandExecutor {

    void execute(CommandSender sender, String[] args) throws Exception;


    CommandWrapper getCommand();


    Map<String, CommandWrapper> getCommandsMap();


    Set<CommandWrapper> getSubCommands();


    Set<String> onTabComplete(CommandSender sender, String[] args);

    interface Reversed<T> extends CommandExecutor {

        void set(T parameter);


        T getParameter();


        Class<T> getParameterType();
    }
}
