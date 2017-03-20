package me.kayaba.guilds.command.admin.guild.rank;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.storage.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.Command;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminGuildRank extends AbstractCommandExecutor.Reversed<Guild> {
    private final boolean admin;

    public CommandAdminGuildRank() {
        this(true);
    }

    public CommandAdminGuildRank(boolean admin) {
        this.admin = admin;

        if (admin) {
            commandsMap.put("list", Command.ADMIN_GUILD_RANK_LIST);
            commandsMap.put("ls", Command.ADMIN_GUILD_RANK_LIST);
            commandsMap.put("del", Command.ADMIN_GUILD_RANK_DELETE);
            commandsMap.put("delete", Command.ADMIN_GUILD_RANK_DELETE);
        } else {
            commandsMap.put("list", Command.GUILD_RANK_LIST);
            commandsMap.put("ls", Command.GUILD_RANK_LIST);
            commandsMap.put("del", Command.GUILD_RANK_DELETE);
            commandsMap.put("delete", Command.GUILD_RANK_DELETE);
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Guild guild;

        if (admin) {
            if (args.length == 0) {
                Message.CHAT_COMMANDS_HEADER_ADMIN_GUILD_RANK.send(sender);
                for (CommandWrapper commandWrapper : getSubCommands()) {
                    commandWrapper.getUsageMessage().send(sender);
                }
                return;
            }

            guild = getParameter();
        } else {
            if (args.length == 0) {
                Message.CHAT_COMMANDS_HEADER_GUILD_RANK.send(sender);
                for (CommandWrapper commandWrapper : getSubCommands()) {
                    commandWrapper.getUsageMessage().send(sender);
                }
                return;
            }

            GPlayer nPlayer = PlayerManager.getPlayer(sender);
            guild = nPlayer.getGuild();
        }

        CommandWrapper subCommand = getSubCommand(args);

        if (subCommand == null) {
            Message.CHAT_UNKNOWNCMD.send(sender);
            return;
        }

        if (guild == null) {
            Message.CHAT_GUILD_COULDNOTFIND.send(sender);
            return;
        }

        int argCut = 1;
        if (subCommand == Command.ADMIN_GUILD_RANK_LIST || subCommand == Command.GUILD_RANK_LIST) {
            subCommand.executorVariable(guild);
        } else {
            Resource rank = null;
            String rankName = args[0];

            for (GRank rankLoop : getParameter().getRanks()) {
                if (rankLoop.getName().equalsIgnoreCase(rankName)) {
                    rank = rankLoop;
                    break;
                }
            }

            if (rank == null) {
                Message.CHAT_ADMIN_GUILD_RANK_NOTFOUND.send(sender);
                return;
            }

            subCommand.executorVariable(rank);
            argCut = 2;
        }

        subCommand.execute(sender, StringUtils.parseArgs(args, argCut));
    }
}
