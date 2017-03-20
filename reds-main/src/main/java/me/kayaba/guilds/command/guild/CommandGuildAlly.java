package me.kayaba.guilds.command.guild;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandGuildAlly extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        Guild guild = nPlayer.getGuild();

        if (args.length > 0) {
            Guild allyGuild = GuildManager.getGuildFind(args[0]);

            if (allyGuild == null) {
                Message.CHAT_GUILD_NAMENOTEXIST.send(sender);
                return;
            }

            if (allyGuild.equals(guild)) {
                Message.CHAT_GUILD_ALLY_SAMENAME.send(sender);
                return;
            }

            Map<VarKey, String> vars = new HashMap<>();
            vars.put(VarKey.GUILD_NAME, guild.getName());
            vars.put(VarKey.ALLYNAME, allyGuild.getName());

            if (!guild.isAlly(allyGuild)) {
                if (guild.isWarWith(allyGuild)) {
                    Message.CHAT_GUILD_ALLY_WAR.clone().vars(vars).send(sender);
                    return;
                }

                if (guild.isInvitedToAlly(allyGuild)) {
                    if (!nPlayer.hasPermission(GuildPermission.ALLY_ACCEPT)) {
                        Message.CHAT_GUILD_NOGUILDPERM.send(sender);
                        return;
                    }

                    allyGuild.addAlly(guild);
                    guild.addAlly(allyGuild);
                    guild.removeAllyInvitation(allyGuild);
                    Message.BROADCAST_GUILD_ALLIED.clone().vars(vars).broadcast();

                    Message.CHAT_GUILD_ALLY_ACCEPTED.clone().vars(vars).send(sender);


                    TagUtils.refresh();
                    TabUtils.refresh();
                } else {
                    if (!allyGuild.isInvitedToAlly(guild)) {
                        if (!nPlayer.hasPermission(GuildPermission.ALLY_INVITE_SEND)) {
                            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
                            return;
                        }

                        allyGuild.addAllyInvitation(guild);
                        Message.CHAT_GUILD_ALLY_INVITED.clone().vars(vars).send(sender);
                        Message.CHAT_GUILD_ALLY_NOTIFYGUILD.clone().vars(vars).broadcast(allyGuild);
                    } else {
                        if (!nPlayer.hasPermission(GuildPermission.ALLY_INVITE_CANCEL)) {
                            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
                            return;
                        }

                        allyGuild.removeAllyInvitation(guild);

                        Message.CHAT_GUILD_ALLY_CANCELED.clone().vars(vars).send(sender);
                        Message.CHAT_GUILD_ALLY_NOTIFYGUILDCANCELED.clone().vars(vars).broadcast(allyGuild);
                    }
                }
            } else {
                if (!nPlayer.hasPermission(GuildPermission.ALLY_CANCEL)) {
                    Message.CHAT_GUILD_NOGUILDPERM.send(sender);
                    return;
                }

                guild.removeAlly(allyGuild);
                allyGuild.removeAlly(guild);

                Message.BROADCAST_GUILD_ENDALLY.clone().vars(vars).broadcast();

                TagUtils.refresh();
                TabUtils.refresh();
            }

            return;
        }


        Message.CHAT_GUILD_ALLY_LIST_HEADER_ALLIES.send(sender);
        MessageWrapper allyRowFormat = Message.CHAT_GUILD_ALLY_LIST_ITEM;

        if (!guild.getAllies().isEmpty()) {
            final Collection<MessageWrapper> alliesSet = new HashSet<>();

            for (Guild guildLoop : guild.getAllies()) {
                alliesSet.add(allyRowFormat.clone().setVar(VarKey.GUILD_NAME, guildLoop.getName()));
            }

            MessageManager.sendMessage(sender, StringUtils.join(alliesSet, Message.CHAT_GUILD_ALLY_LIST_SEPARATOR));
        } else {
            Message.CHAT_GUILD_ALLY_LIST_NOALLIES.send(sender);
        }

        if (!guild.getAllyInvitations().isEmpty()) {
            Message.CHAT_GUILD_ALLY_LIST_HEADER_INVITATIONS.send(sender);

            final Collection<MessageWrapper> allyInvitationSet = new HashSet<>();
            for (Guild guildLoop : guild.getAllyInvitations()) {
                allyInvitationSet.add(allyRowFormat.clone().setVar(VarKey.GUILD_NAME, guildLoop.getName()));
            }

            MessageManager.sendMessage(sender, StringUtils.join(allyInvitationSet, Message.CHAT_GUILD_ALLY_LIST_SEPARATOR));
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
