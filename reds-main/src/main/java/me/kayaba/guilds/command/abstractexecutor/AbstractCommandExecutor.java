package me.kayaba.guilds.command.abstractexecutor;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.CommandExecutor;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.Command;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.lang.reflect.*;
import java.util.*;

public abstract class AbstractCommandExecutor implements CommandExecutor {
    protected final PracticeServer plugin = PracticeServer.getInstance();
    public final Map<String, CommandWrapper> commandsMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @Override
    public final CommandWrapper getCommand() {
        return Command.getCommand(this);
    }

    @Override
    public final Map<String, CommandWrapper> getCommandsMap() {
        return commandsMap;
    }

    @Override
    public Set<CommandWrapper> getSubCommands() {
        return new HashSet<>(commandsMap.values());
    }

    @Override
    public Set<String> onTabComplete(CommandSender sender, String[] args) {
        Set<String> set = new HashSet<>();

        if (args.length > 0) {
            for (String key : tabCompleteOptions(sender, args)) {
                if (key.startsWith(args[args.length - 1])) {
                    set.add(key);
                }
            }
        } else {
            set.addAll(getCommandsMap().keySet());
        }

        return set;
    }

    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        return getCommandsMap().keySet();
    }


    protected CommandWrapper getSubCommand(String[] args) {
        int subCommandArg = 0;

        if (args.length >= 2
                && getCommandsMap().get(args[1]) != null
                && getCommandsMap().get(args[1]).isReversed()) {
            subCommandArg = 1;
        }

        return getCommandsMap().get(args[subCommandArg]);
    }


    protected final void dumpArgs(String[] args) {
        int index = 0;
        LoggerUtils.debug("Command arguments dump. (" + args.length + " items)");
        for (String arg : args) {
            LoggerUtils.debug(index + ": " + arg);
            index++;
        }
    }

    public static abstract class Reversed<T> extends AbstractCommandExecutor implements CommandExecutor.Reversed<T> {
        protected T parameter;

        @Override
        public final void set(T parameter) {
            this.parameter = parameter;
        }

        @Override
        public final T getParameter() {
            return parameter;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Class<T> getParameterType() {
            return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
    }
}
