package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandGuildKick extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        Guild guild = nPlayer.getGuild();

        if (!nPlayer.hasPermission(GuildPermission.KICK)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        if (args.length == 0) {
            Message.CHAT_PLAYER_ENTERNAME.send(sender);
            return;
        }

        GPlayer nPlayerKick = PlayerManager.getPlayer(args[0]);

        if (nPlayerKick == null) {
            Message.CHAT_PLAYER_NOTEXISTS.send(sender);
            return;
        }

        if (!nPlayerKick.hasGuild()) {
            Message.CHAT_PLAYER_HASNOGUILD.send(sender);
            return;
        }

        if (!nPlayerKick.getGuild().getName().equalsIgnoreCase(guild.getName())) {
            Message.CHAT_PLAYER_NOTINYOURGUILD.send(sender);
            return;
        }

        if (nPlayer.getName().equalsIgnoreCase(nPlayerKick.getName())) {
            Message.CHAT_GUILD_KICKYOURSELF.send(sender);
            return;
        }


        guild.removePlayer(nPlayerKick);
        nPlayerKick.cancelToolProgress();

        if (nPlayerKick.isOnline()) {
            guild.hideVaultHologram(nPlayerKick.getPlayer());
        }

        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.PLAYER_NAME, nPlayerKick.getName());
        vars.put(VarKey.GUILD_NAME, guild.getName());
        Message.BROADCAST_GUILD_KICKED.clone().vars(vars).broadcast();


        TagUtils.refresh();
        TabUtils.refresh();
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        Set<String> options = new HashSet<>();
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (nPlayer.hasGuild()) {
            for (GPlayer guildMember : nPlayer.getGuild().getPlayers()) {
                if (!guildMember.isLeader() && !guildMember.equals(nPlayer)) {
                    options.add(guildMember.getName());
                }
            }
        }

        return options;
    }
}
