package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandGuildTop extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Collection<Guild> guilds = plugin.getGuildManager().getGuilds();

        if (guilds.isEmpty()) {
            Message.CHAT_GUILD_NOGUILDS.send(sender);
            return;
        }

        int limit = Config.LEADERBOARD_GUILD_ROWS.getInt();
        int i = 1;

        Message.HOLOGRAPHICDISPLAYS_TOPGUILDS_HEADER.send(sender);

        Map<VarKey, String> vars = new HashMap<>();
        for (Guild guild : plugin.getGuildManager().getTopGuildsByPoints(limit)) {
            vars.clear();
            vars.put(VarKey.GUILD_NAME, guild.getName());
            vars.put(VarKey.N, String.valueOf(i));
            vars.put(VarKey.GUILD_POINTS, String.valueOf(guild.getPoints()));
            Message.HOLOGRAPHICDISPLAYS_TOPGUILDS_ROW.clone().vars(vars).send(sender);
            i++;
        }
    }
}
