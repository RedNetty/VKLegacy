package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandGuildLeader extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length != 1) {
            Message.CHAT_PLAYER_ENTERNAME.send(sender);
            return;
        }

        GPlayer nPlayer = PlayerManager.getPlayer(sender);
        GPlayer newLeader = PlayerManager.getPlayer(args[0]);

        if (newLeader == null) {
            Message.CHAT_PLAYER_NOTEXISTS.send(sender);
            return;
        }

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        Guild guild = nPlayer.getGuild();

        if (!nPlayer.isLeader()) {
            Message.CHAT_GUILD_NOTLEADER.send(sender);
            return;
        }

        if (newLeader.equals(nPlayer)) {
            Message.CHAT_GUILD_LEADER_SAMENICK.send(sender);
            return;
        }

        if (!newLeader.hasGuild() || !guild.isMember(newLeader)) {
            Message.CHAT_GUILD_LEADER_NOTSAMEGUILD.send(sender);
            return;
        }

        guild.getLeader().cancelToolProgress();


        guild.setLeader(newLeader);
        plugin.getGuildManager().save(guild);

        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.PLAYER_NAME, newLeader.getName());
        vars.put(VarKey.GUILD_NAME, guild.getName());
        Message.CHAT_GUILD_LEADER_SUCCESS.clone().vars(vars).send(sender);
        Message.BROADCAST_GUILD_SETLEADER.clone().vars(vars).broadcast();


        TagUtils.refresh();
        TabUtils.refresh();
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        Set<String> options = new HashSet<>();

        if (args.length == 0) {
            return options;
        }

        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (nPlayer.hasGuild()) {
            for (GPlayer guildMember : nPlayer.getGuild().getPlayers()) {
                if (!guildMember.isLeader()) {
                    options.add(guildMember.getName());
                }
            }
        }

        return options;
    }
}
