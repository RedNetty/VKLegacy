package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.event.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandAdminGuildPurge extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (plugin.getGuildManager().getGuilds().isEmpty()) {
            Message.CHAT_GUILD_NOGUILDS.send(sender);
            return;
        }

        for (Guild guild : new ArrayList<>(plugin.getGuildManager().getGuilds())) {

            GuildAbandonEvent guildAbandonEvent = new GuildAbandonEvent(guild, AbandonCause.ADMIN_ALL);
            plugin.getServer().getPluginManager().callEvent(guildAbandonEvent);


            if (!guildAbandonEvent.isCancelled()) {

                plugin.getGuildManager().delete(guildAbandonEvent);

                Map<VarKey, String> vars = new HashMap<>();
                vars.put(VarKey.PLAYER_NAME, sender.getName());
                vars.put(VarKey.GUILD_NAME, guild.getName());
                Message.BROADCAST_ADMIN_GUILD_ABANDON.clone().vars(vars).broadcast();
            }
        }
    }
}
