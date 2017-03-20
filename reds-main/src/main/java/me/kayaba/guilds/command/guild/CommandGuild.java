package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.Command;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class CommandGuild extends AbstractCommandExecutor {
    public CommandGuild() {
        commandsMap.put("pay", Command.GUILD_BANK_PAY);
        commandsMap.put("withdraw", Command.GUILD_BANK_WITHDRAW);
        commandsMap.put("leader", Command.GUILD_LEADER);
        commandsMap.put("info", Command.GUILD_INFO);
        commandsMap.put("leave", Command.GUILD_LEAVE);
        commandsMap.put("menu", Command.GUILD_MENU);
        commandsMap.put("ally", Command.GUILD_ALLY);
        commandsMap.put("kick", Command.GUILD_KICK);
        commandsMap.put("abandon", Command.GUILD_ABANDON);
        commandsMap.put("invite", Command.GUILD_INVITE);
        commandsMap.put("join", Command.GUILD_JOIN);
        commandsMap.put("create", Command.GUILD_CREATE);
        commandsMap.put("war", Command.GUILD_WAR);
        //commandsMap.put("compass",  Command.GUILD_COMPASS); // Reserved for later use!
        commandsMap.put("effect", Command.GUILD_EFFECT);
        commandsMap.put("top", Command.GUILD_TOP);
        //commandsMap.put("items",    Command.GUILD_REQUIREDITEMS); // Reserved for later use!
        commandsMap.put("pvp", Command.GUILD_PVPTOGGLE);
        commandsMap.put("buylife", Command.GUILD_BUYLIFE);
        commandsMap.put("buyslot", Command.GUILD_BUYSLOT);
        commandsMap.put("c", Command.GUILD_CHATMODE);
        commandsMap.put("chat", Command.GUILD_CHATMODE);
        commandsMap.put("chatmode", Command.GUILD_CHATMODE);
        commandsMap.put("openinv", Command.GUILD_OPENINVITATION);
        commandsMap.put("setname", Command.GUILD_SET_NAME);
        commandsMap.put("name", Command.GUILD_SET_NAME);
        commandsMap.put("settag", Command.GUILD_SET_TAG);
        commandsMap.put("tag", Command.GUILD_SET_TAG);
        commandsMap.put("pi", Command.PLAYERINFO);
        commandsMap.put("rank", Command.GUILD_RANK_ACCESS);
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (args.length > 0) {
            CommandWrapper subCommand = getSubCommand(args);
            String[] newArgs = StringUtils.parseArgs(args, 1);

            if (subCommand == null) {
                Message.CHAT_UNKNOWNCMD.send(sender);
            } else {
                if (subCommand.isReversed() && subCommand == Command.GUILD_RANK_ACCESS) {
                    subCommand.executorVariable(nPlayer.getGuild());
                }

                subCommand.execute(sender, newArgs);
            }
        } else {
            if (!(sender instanceof Player)) {
                Message.CHAT_CMDFROMCONSOLE.send(sender);
                return;
            }

            if (nPlayer.hasGuild()) {
                for (String message : Message.CHAT_COMMANDS_GUILD_HASGUILD.getList()) {
                    GuildPermission guildPermission = null;
                    if (message.startsWith("{") && org.apache.commons.lang.StringUtils.contains(message, "}")) {
                        message = message.substring(1);
                        String[] split = org.apache.commons.lang.StringUtils.split(message, '}');
                        guildPermission = GuildPermission.fromString(split[0]);

                        if (split.length == 2) {
                            message = split[1];
                        } else {
                            split[0] = "";
                            message = StringUtils.join(split, "}");
                        }
                    }

                    if (guildPermission == null || nPlayer.hasPermission(guildPermission)) {
                        MessageManager.sendMessage(sender, message);
                    }
                }
            } else {
                Message.CHAT_COMMANDS_GUILD_NOGUILD.prefix(false).send(sender);
            }
        }
    }
}
