package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class CommandGuildCompass extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Player player = (Player) sender;
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        if (nPlayer.getPreferences().isCompassPointingGuild()) {
            nPlayer.getPreferences().setCompassPointingGuild(false);
            player.setCompassTarget(player.getWorld().getSpawnLocation());
            Message.CHAT_GUILD_COMPASSTARGET_OFF.send(sender);
        } else {
            nPlayer.getPreferences().setCompassPointingGuild(true);
            player.setCompassTarget(nPlayer.getGuild().getHome());
            Message.CHAT_GUILD_COMPASSTARGET_ON.send(sender);
        }
    }
}
