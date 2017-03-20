package me.kayaba.guilds.command.admin;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.Command;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdmin extends AbstractCommandExecutor {
    public CommandAdmin() {
        commandsMap.put("guild", Command.ADMIN_GUILD_ACCESS);
        commandsMap.put("g", Command.ADMIN_GUILD_ACCESS);
        commandsMap.put("reload", Command.ADMIN_RELOAD);
        commandsMap.put("save", Command.ADMIN_SAVE);
        commandsMap.put("spy", Command.ADMIN_CHATSPY);
        commandsMap.put("chatspy", Command.ADMIN_CHATSPY);
        commandsMap.put("config", Command.ADMIN_CONFIG_ACCESS);
        commandsMap.put("player", Command.ADMIN_PLAYER_ACCESS);
        commandsMap.put("p", Command.ADMIN_PLAYER_ACCESS);
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length == 0) {
            Message.CHAT_COMMANDS_HEADER_ADMIN_MAIN.send(sender);
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
