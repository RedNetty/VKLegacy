package me.kayaba.guilds.impl.basic;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.CommandExecutor;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class CommandWrapperImpl implements CommandWrapper {
    protected final Collection<Flag> flags = new HashSet<>();
    protected Class<? extends CommandExecutor> clazz;
    protected Permission permission;
    protected TabCompleter tabCompleter;
    protected CommandExecutor executor;
    protected MessageWrapper usageMessage;
    protected Resource executorVariable;
    protected String name;
    protected String genericCommand;

    @Override
    public Permission getPermission() {
        return permission;
    }

    @Override
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return permission.has(sender);
    }

    @Override
    public TabCompleter getTabCompleter() {
        return tabCompleter;
    }

    @Override
    public void setTabCompleter(TabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }

    @Override
    public boolean hasTabCompleter() {
        return tabCompleter != null;
    }

    @Override
    public void setFlags(Flag... flags) {
        this.flags.clear();
        Collections.addAll(this.flags, flags);
    }

    @Override
    public boolean hasFlag(Flag flag) {
        return flags.contains(flag);
    }

    @Override
    public Collection<Flag> getFlags() {
        return new HashSet<>(flags);
    }

    @Override
    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public CommandExecutor getExecutor() {
        return executor;
    }

    @Override
    public Class<? extends CommandExecutor> getExecutorClass() {
        return clazz;
    }

    @Override
    public boolean isReversed() {
        return CommandExecutor.Reversed.class.isAssignableFrom(getExecutorClass());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PracticeServer.getInstance().getCommandManager().execute(this, sender, args);
    }

    @Override
    public MessageWrapper getUsageMessage() {
        return usageMessage;
    }

    @Override
    public void setUsageMessage(MessageWrapper message) {
        this.usageMessage = message;
    }

    @Override
    public boolean allowedSender(CommandSender sender) {
        return sender instanceof Player || !hasFlag(Flag.NOCONSOLE);
    }

    @Override
    public Resource getExecutorVariable() {
        return executorVariable;
    }

    @Override
    public void executorVariable(Resource resource) {
        executorVariable = resource;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean hasGenericCommand() {
        return genericCommand != null;
    }

    @Override
    public String getGenericCommand() {
        return genericCommand;
    }

    @Override
    public void setGenericCommand(String genericCommand) {
        this.genericCommand = genericCommand;
    }
}
