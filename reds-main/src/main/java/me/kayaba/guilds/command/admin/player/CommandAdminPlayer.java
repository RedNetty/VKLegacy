package me.kayaba.guilds.command.admin.player;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.Command;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminPlayer extends AbstractCommandExecutor {
    public CommandAdminPlayer() {
        commandsMap.put("setpoints", Command.ADMIN_PLAYER_SET_POINTS);
        commandsMap.put("points", Command.ADMIN_PLAYER_SET_POINTS);
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length == 0) {
            Message.CHAT_COMMANDS_HEADER_ADMIN_PLAYER.send(sender);
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

        if (subCommand.isReversed()) {
            GPlayer nPlayer = PlayerManager.getPlayer(args[0]);

            if (nPlayer == null) {
                Message.CHAT_PLAYER_NOTEXISTS.send(sender);
                return;
            }

            subCommand.executorVariable(nPlayer);
        }

        subCommand.execute(sender, StringUtils.parseArgs(args, !subCommand.isReversed() ? 1 : 2));
    }
}
