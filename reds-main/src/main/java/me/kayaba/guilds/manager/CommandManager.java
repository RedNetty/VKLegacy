package me.kayaba.guilds.manager;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.CommandExecutor;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.Command;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

import java.util.*;

public class CommandManager {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private final Map<String, String> aliases = new HashMap<>();
    private final Map<CommandWrapper, CommandExecutor> executors = new HashMap<>();
    private static final org.bukkit.command.CommandExecutor genericExecutor = new GenericExecutor();


    public void setUp() {
        Command.init();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("aliases");

        for (String key : section.getKeys(false)) {
            for (String alias : section.getStringList(key)) {
                aliases.put(alias, key);
            }
        }

        LoggerUtils.info("Enabled");
    }


    public String getMainCommand(String alias) {
        return aliases.get(alias);
    }


    public boolean existsAlias(String alias) {
        return aliases.containsKey(alias);
    }


    public void registerExecutor(CommandWrapper command, CommandExecutor executor) {
        if (!executors.containsKey(command)) {
            executors.put(command, executor);

            if (command.hasGenericCommand()) {
                PluginCommand genericCommand = plugin.getCommand(command.getGenericCommand());

                if (executor instanceof org.bukkit.command.CommandExecutor) {
                    genericCommand.setExecutor((org.bukkit.command.CommandExecutor) executor);
                } else {
                    genericCommand.setExecutor(genericExecutor);
                }
            }
        }
    }


    public void execute(CommandWrapper command, CommandSender sender, String[] args) {
        CommandExecutor executor = getExecutor(command);

        if (!command.hasPermission(sender)) {
            Message.CHAT_NOPERMISSIONS.send(sender);
            return;
        }

        if (!command.allowedSender(sender)) {
            Message.CHAT_CMDFROMCONSOLE.send(sender);
            return;
        }

        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if ((sender instanceof Player) && (command.hasFlag(CommandWrapper.Flag.CONFIRM) && !Permission.GUILDS_ADMIN_NOCONFIRM.has(sender) && (nPlayer.getCommandExecutorHandler() == null || nPlayer.getCommandExecutorHandler().getState() != CommandExecutorHandler.State.CONFIRMED))) {
            nPlayer.newCommandExecutorHandler(command, args);
            nPlayer.getCommandExecutorHandler().executorVariable(command.getExecutorVariable());
        } else {
            if (executor instanceof CommandExecutor.Reversed) {
                ((CommandExecutor.Reversed) executor).set(command.getExecutorVariable());
                command.executorVariable(null);
            }

            try {
                executor.execute(sender, args);
            } catch (Exception e) {
                LoggerUtils.exception(new CommandException("Unhandled exception executing command '" + command.getName() + "' in plugin KayabaGuilds", e));
            }
        }
    }


    public CommandExecutor getExecutor(CommandWrapper command) {
        return executors.get(command);
    }

    public static class GenericExecutor implements org.bukkit.command.CommandExecutor, TabCompleter {
        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            CommandWrapper commandWrapper = Command.getByGenericCommand(command.getName());

            if (commandWrapper == null) {
                return false;
            }

            commandWrapper.execute(sender, StringUtils.parseQuotedArguments(args));
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
            CommandWrapper commandWrapper = Command.getByGenericCommand(command.getName());
            CommandWrapper finalCommand = commandWrapper;

            if (commandWrapper == null) {
                return new ArrayList<>();
            }

            int index = 0, lastSuccessfulIndex = 0;
            while (index < args.length) {
                CommandWrapper finalCommandCheck = finalCommand.getExecutor().getCommandsMap().get(args[index]);

                if (finalCommandCheck != null) {
                    finalCommand = finalCommandCheck;
                    lastSuccessfulIndex = index;
                }

                index++;
            }

            if (finalCommand != commandWrapper) {
                lastSuccessfulIndex++;
            }

            return new ArrayList<>(finalCommand.getExecutor().onTabComplete(sender, StringUtils.parseArgs(args, lastSuccessfulIndex)));
        }
    }
}
