package me.kayaba.guilds.command.guild;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.*;
import org.bukkit.command.*;

import java.text.*;
import java.util.*;

public class CommandGuildWar extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        String dayNames[] = new DateFormatSymbols().getWeekdays();
        Calendar date1 = Calendar.getInstance();
        if(dayNames[date1.get(Calendar.DAY_OF_WEEK)] == "Saturday" || dayNames[date1.get(Calendar.DAY_OF_WEEK)] == "Sunday")
        {
           nPlayer.getPlayer().sendMessage(ChatColor.WHITE + "The Guild War hasn't started yet. The Guild War is in effect from Monday - Friday!");
            return;
        }

        if (!nPlayer.hasGuild()) {
            Message.CHAT_PLAYER_HASNOGUILD.send(sender);
            return;
        }

        Guild guild = nPlayer.getGuild();

        if (args.length == 0) {
            Message.CHAT_GUILD_WAR_LIST_WARSHEADER.send(sender);
            MessageWrapper guildNameFormat = Message.CHAT_GUILD_WAR_LIST_ITEM;

            if (!guild.getWars().isEmpty()) {
                final Collection<MessageWrapper> warNamesSet = new HashSet<>();
                for (Guild guildLoop : guild.getWars()) {
                    warNamesSet.add(guildNameFormat.clone().setVar(VarKey.GUILD_NAME, guildLoop.getName()));
                }

                MessageManager.sendPrefixMessage(sender, StringUtils.join(warNamesSet, Message.CHAT_GUILD_WAR_LIST_SEPARATOR));
            } else {
                Message.CHAT_GUILD_WAR_LIST_NOWARS.send(sender);
            }

            if (!guild.getNoWarInvitations().isEmpty()) {
                Message.CHAT_GUILD_WAR_LIST_NOWARINVHEADER.send(sender);
                final Collection<MessageWrapper> noWarInvitationNamesSet = new HashSet<>();

                for (Guild guildLoop : guild.getNoWarInvitations()) {
                    noWarInvitationNamesSet.add(guildNameFormat.clone().setVar(VarKey.GUILD_NAME, guildLoop.getName()));
                }

                MessageManager.sendPrefixMessage(sender, StringUtils.join(noWarInvitationNamesSet, Message.CHAT_GUILD_WAR_LIST_SEPARATOR));
            }

            return;
        }

        String guildName = args[0];

        Guild cmdGuild = GuildManager.getGuildFind(guildName);

        if (cmdGuild == null) {
            Message.CHAT_GUILD_COULDNOTFIND.send(sender);
            return;
        }

        if (guild.isWarWith(cmdGuild)) {
            Map<VarKey, String> vars = new HashMap<>();

            if (guild.isNoWarInvited(cmdGuild)) {
                if (!nPlayer.hasPermission(GuildPermission.WAR_INVITE_ACCEPT)) {
                    Message.CHAT_GUILD_NOGUILDPERM.send(sender);
                    return;
                }

                guild.removeNoWarInvitation(cmdGuild);
                guild.removeWar(cmdGuild);
                cmdGuild.removeWar(guild);


                vars.put(VarKey.GUILD1, guild.getName());
                vars.put(VarKey.GUILD2, cmdGuild.getName());
                Message.BROADCAST_GUILD_NOWAR.vars(vars).broadcast();
            } else {
                if (cmdGuild.isNoWarInvited(guild)) {
                    if (!nPlayer.hasPermission(GuildPermission.WAR_INVITE_CANCEL)) {
                        Message.CHAT_GUILD_NOGUILDPERM.send(sender);
                        return;
                    }

                    cmdGuild.removeNoWarInvitation(guild);
                    Message.CHAT_GUILD_WAR_NOWAR_CANCEL_SUCCESS.clone().setVar(VarKey.GUILD_NAME, cmdGuild.getName()).send(sender);
                    Message.CHAT_GUILD_WAR_NOWAR_CANCEL_NOTIFY.clone().setVar(VarKey.GUILD_NAME, guild.getName()).broadcast(cmdGuild);
                } else {
                    if (!nPlayer.hasPermission(GuildPermission.WAR_INVITE_SEND)) {
                        Message.CHAT_GUILD_NOGUILDPERM.send(sender);
                        return;
                    }

                    cmdGuild.addNoWarInvitation(guild);
                    vars.put(VarKey.GUILD_NAME, cmdGuild.getName());
                    Message.CHAT_GUILD_WAR_NOWAR_INVITE_SUCCESS.vars(vars).send(sender);


                    vars.clear();
                    vars.put(VarKey.GUILD_NAME, guild.getName());
                    Message.CHAT_GUILD_WAR_NOWAR_INVITE_NOTIFY.vars(vars).broadcast(cmdGuild);
                }
            }
        } else {
            if (!nPlayer.hasPermission(GuildPermission.WAR_START)) {
                Message.CHAT_GUILD_NOGUILDPERM.send(sender);
                return;
            }

            if (guild.getName().equalsIgnoreCase(cmdGuild.getName())) {
                Message.CHAT_GUILD_WAR_YOURGUILDWAR.send(sender);
                return;
            }

            if (guild.isAlly(cmdGuild)) {
                Message.CHAT_GUILD_WAR_ALLY.send(sender);
                return;
            }

            guild.addWar(cmdGuild);
            cmdGuild.addWar(guild);


            Map<VarKey, String> vars = new HashMap<>();
            vars.put(VarKey.GUILD1, guild.getName());
            vars.put(VarKey.GUILD2, cmdGuild.getName());
            Message.BROADCAST_GUILD_WAR.vars(vars).broadcast();
            TagUtils.refresh();
            TabUtils.refresh();
            plugin.getRegionManager().checkRaidInit(nPlayer);
        }
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        Set<String> options = new HashSet<>();
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        for (Guild guild : PracticeServer.getInstance().getGuildManager().getGuilds()) {
            if (!nPlayer.hasGuild() || !guild.equals(nPlayer.getGuild())) {
                options.add(guild.getTag().toLowerCase());
                options.add(guild.getName().toLowerCase());
            }
        }

        return options;
    }
}
