package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.event.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.*;

import java.util.*;

public class CommandGuildAbandon extends AbstractCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return true;
        }

        if (!nPlayer.hasPermission(GuildPermission.ABANDON)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return true;
        }

        getCommand().execute(sender, args);
        return true;
    }

    public void execute(CommandSender sender, String args[]) {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        if (!nPlayer.hasPermission(GuildPermission.ABANDON)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        Guild guild = nPlayer.getGuild();


        GuildAbandonEvent guildAbandonEvent = new GuildAbandonEvent(guild, AbandonCause.PLAYER);
        plugin.getServer().getPluginManager().callEvent(guildAbandonEvent);


        if (!guildAbandonEvent.isCancelled()) {
            plugin.getGuildManager().delete(guildAbandonEvent);

            Message.CHAT_GUILD_ABANDONED.send(sender);

            Map<VarKey, String> vars = new HashMap<>();
            vars.put(VarKey.PLAYER_NAME, sender.getName());
            vars.put(VarKey.GUILD_NAME, guild.getName());
            Message.BROADCAST_GUILD_ABANDONED.clone().vars(vars).broadcast();
            TagUtils.refresh();
            TabUtils.refresh();
        }
    }
}
