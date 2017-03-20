package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.Command;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminGuild extends AbstractCommandExecutor {
    public CommandAdminGuild() {
        commandsMap.put("abandon", Command.ADMIN_GUILD_ABANDON);
        commandsMap.put("setname", Command.ADMIN_GUILD_SET_NAME);
        commandsMap.put("name", Command.ADMIN_GUILD_SET_NAME);
        commandsMap.put("settag", Command.ADMIN_GUILD_SET_TAG);
        commandsMap.put("tag", Command.ADMIN_GUILD_SET_TAG);
        commandsMap.put("setpoints", Command.ADMIN_GUILD_SET_POINTS);
        commandsMap.put("points", Command.ADMIN_GUILD_SET_POINTS);
        commandsMap.put("setslots", Command.ADMIN_GUILD_SET_SLOTS);
        commandsMap.put("slots", Command.ADMIN_GUILD_SET_SLOTS);
        commandsMap.put("promote", Command.ADMIN_GUILD_SET_LEADER);
        commandsMap.put("leader", Command.ADMIN_GUILD_SET_LEADER);
        commandsMap.put("setleader", Command.ADMIN_GUILD_SET_LEADER);
        commandsMap.put("invite", Command.ADMIN_GUILD_INVITE);
        commandsMap.put("pay", Command.ADMIN_GUILD_BANK_PAY);
        commandsMap.put("withdraw", Command.ADMIN_GUILD_BANK_WITHDRAW);
        commandsMap.put("timerest", Command.ADMIN_GUILD_SET_TIMEREST);
        commandsMap.put("liveregentime", Command.ADMIN_GUILD_SET_LIVEREGENERATIONTIME);
        commandsMap.put("lives", Command.ADMIN_GUILD_SET_LIVES);
        commandsMap.put("purge", Command.ADMIN_GUILD_PURGE);
        commandsMap.put("list", Command.ADMIN_GUILD_LIST);
        commandsMap.put("inactive", Command.ADMIN_GUILD_INACTIVE);
        commandsMap.put("kick", Command.ADMIN_GUILD_KICK);
        commandsMap.put("resetpoints", Command.ADMIN_GUILD_RESET_POINTS);
        commandsMap.put("rank", Command.ADMIN_GUILD_RANK_ACCESS);
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {

        if (args.length == 0) {
            Message.CHAT_COMMANDS_HEADER_ADMIN_GUILD_MAIN.send(sender);
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
            Guild guild = GuildManager.getGuildFind(args[0]);

            if (guild == null) {
                Message.CHAT_GUILD_COULDNOTFIND.send(sender);
                return;
            }

            subCommand.executorVariable(guild);
        }

        subCommand.execute(sender, StringUtils.parseArgs(args, !subCommand.isReversed() ? 1 : 2));
    }
}
