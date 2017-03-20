package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class CommandAdminGuildSetLeader extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length == 0) {
            Message.CHAT_PLAYER_ENTERNAME.send(sender);
            return;
        }

        String playerName = args[0];

        GPlayer nPlayer = PlayerManager.getPlayer(playerName);

        if (nPlayer == null) {
            Message.CHAT_PLAYER_NOTEXISTS.send(sender);
            return;
        }

        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.PLAYER_NAME, nPlayer.getName());

        if (!nPlayer.hasGuild()) {
            Message.CHAT_PLAYER_HASNOGUILD.send(sender);
            return;
        }

        Guild guild = nPlayer.getGuild();
        vars.put(VarKey.GUILD_NAME, guild.getName());

        if (!guild.isMember(nPlayer)) {
            Message.CHAT_ADMIN_GUILD_SET_LEADER_NOTINGUILD.clone().vars(vars).send(sender);
            return;
        }

        if (nPlayer.isLeader()) {
            Message.CHAT_ADMIN_GUILD_SET_LEADER_ALREADYLEADER.clone().vars(vars).send(sender);
            return;
        }

        Player oldLeader = guild.getLeader().getPlayer();

        guild.getLeader().cancelToolProgress();

        guild.setLeader(nPlayer);

        if (oldLeader != null) {
            TagUtils.refresh(oldLeader);
            TabUtils.refresh(oldLeader);
        }

        if (nPlayer.isOnline()) {
            TagUtils.refresh(nPlayer.getPlayer());
            TabUtils.refresh(nPlayer);
        }

        Message.CHAT_ADMIN_GUILD_SET_LEADER_SUCCESS.clone().vars(vars).send(sender);
        Message.BROADCAST_GUILD_NEWLEADER.clone().vars(vars).broadcast();
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        Set<String> options = new HashSet<>();

        if (args.length == 0) {
            return options;
        }

        for (GPlayer guildMember : plugin.getPlayerManager().getOnlinePlayers()) {
            if (!guildMember.isLeader()) {
                options.add(guildMember.getName());
            }
        }

        return options;
    }
}
