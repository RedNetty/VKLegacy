package me.kayaba.guilds.command.admin;

import com.gmail.filoghost.holographicdisplays.api.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminReload extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Message.CHAT_RELOAD_RELOADING.send(sender);


        if (Config.HOLOGRAPHICDISPLAYS_ENABLED.getBoolean()) {
            for (Hologram h : HologramsAPI.getHolograms(plugin)) {
                h.delete();
            }
        }


        plugin.getConfigManager().reload();
        Message.CHAT_RELOAD_CONFIG.send(sender);


        plugin.setUpStorage();
        Message.CHAT_RELOAD_MYSQL.send(sender);


        if (!plugin.getMessageManager().existsFile()) {
            Message.CHAT_RELOAD_NEWMSGFILE.send(sender);
        }

        plugin.getMessageManager().load();

        Message.CHAT_RELOAD_MESSAGES.send(sender);


        plugin.getGuildManager().load();
        Message.CHAT_RELOAD_GUILDS.send(sender);


        plugin.getRegionManager().load();
        Message.CHAT_RELOAD_REGIONS.send(sender);


        plugin.getPlayerManager().load();
        Message.CHAT_RELOAD_PLAYERS.send(sender);


        plugin.getGroupManager().load();
        Message.CHAT_RELOAD_GROUPS.send(sender);


        plugin.getRankManager().load();
        Message.CHAT_RELOAD_RANKS.send(sender);

        LoggerUtils.info("Post checks running");
        plugin.getGuildManager().postCheck();


        Message.CHAT_RELOAD_RELOADED.send(sender);
    }
}
