package me.kayaba.guilds.command.admin;

import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminSave extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "players":
                    plugin.getPlayerManager().save();
                    Message.CHAT_ADMIN_SAVE_PLAYERS.send(sender);
                    LoggerUtils.info("Saved players");
                    break;
                case "guilds":
                    plugin.getGuildManager().save();
                    Message.CHAT_ADMIN_SAVE_GUILDS.send(sender);
                    LoggerUtils.info("Saved guilds");
                    break;
                case "regions":
                    plugin.getRegionManager().save();
                    Message.CHAT_ADMIN_SAVE_REGIONS.send(sender);
                    LoggerUtils.info("Saved regions");
                    break;
                case "ranks":
                    plugin.getRankManager().save();
                    Message.CHAT_ADMIN_SAVE_RANKS.send(sender);
                    LoggerUtils.info("Saved ranks");
                    break;
                default:
                    Message.CHAT_INVALIDPARAM.send(sender);
                    break;
            }
        } else {
            plugin.getRegionManager().save();
            plugin.getGuildManager().save();
            plugin.getPlayerManager().save();
            plugin.getRankManager().save();
            Message.CHAT_ADMIN_SAVE_ALL.send(sender);
            LoggerUtils.info("Saved all data");
        }
    }
}
