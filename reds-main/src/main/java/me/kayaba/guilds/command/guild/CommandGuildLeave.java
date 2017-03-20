package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandGuildLeave extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        Guild guild = nPlayer.getGuild();

        if (nPlayer.isLeader()) {
            Message.CHAT_GUILD_LEAVE_ISLEADER.send(sender);
            return;
        }

        guild.removePlayer(nPlayer);
        nPlayer.cancelToolProgress();

        if (nPlayer.isOnline()) {
            guild.hideVaultHologram(nPlayer.getPlayer());
        }

        Message.CHAT_GUILD_LEAVE_LEFT.send(sender);

        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.PLAYER_NAME, sender.getName());
        vars.put(VarKey.GUILD_NAME, guild.getName());
        Message.BROADCAST_GUILD_LEFT.clone().vars(vars).broadcast();

        TagUtils.refresh();
        TabUtils.refresh();
    }
}
