package me.kayaba.guilds.command.admin.config;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.Command;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminConfig extends AbstractCommandExecutor {
    public CommandAdminConfig() {
        commandsMap.put("get", Command.ADMIN_CONFIG_GET);
        commandsMap.put("reload", Command.ADMIN_CONFIG_RELOAD);
        commandsMap.put("reset", Command.ADMIN_CONFIG_RESET);
        commandsMap.put("save", Command.ADMIN_CONFIG_SAVE);
        commandsMap.put("set", Command.ADMIN_CONFIG_SET);
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length == 0) {
            Message.CHAT_COMMANDS_HEADER_ADMIN_CONFIG.send(sender);
            for (CommandWrapper commandWrapper : getSubCommands()) {
                commandWrapper.getUsageMessage().send(sender);
            }
            return;
        }

        CommandWrapper subCommand = getSubCommand(args);

        if (subCommand == null) {
            Message.CHAT_UNKNOWNCMD.send(sender);
            return;
        }

        subCommand.execute(sender, StringUtils.parseArgs(args, 1));
    }
}
