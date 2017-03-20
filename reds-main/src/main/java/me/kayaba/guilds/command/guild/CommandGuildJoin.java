package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandGuildJoin extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);
        List<Guild> invitedTo = nPlayer.getInvitedTo();

        if (nPlayer.hasGuild()) {
            Message.CHAT_CREATEGUILD_HASGUILD.send(sender);
            return;
        }

        if (invitedTo.isEmpty() && args.length != 1) {
            Message.CHAT_PLAYER_INVITE_LIST_NOTHING.send(sender);
            return;
        }

        String guildName;


        if (invitedTo.size() == 1) {
            if (args.length == 0) {
                guildName = invitedTo.get(0).getName();
            } else {
                guildName = args[0];
            }
        } else {
            if (args.length == 0) {
                Message.CHAT_PLAYER_INVITE_LIST_HEADER.send(sender);
                Collection<MessageWrapper> invitedGuildNamesSet = new HashSet<>();

                for (Guild invitedGuild : invitedTo) {
                    invitedGuildNamesSet.add(Message.CHAT_PLAYER_INVITE_LIST_ITEM
                            .clone()
                            .setVar(VarKey.GUILD_NAME, invitedGuild.getName())
                            .setVar(VarKey.TAG, invitedGuild.getTag()));
                }

                sender.sendMessage(StringUtils.join(invitedGuildNamesSet, Message.CHAT_PLAYER_INVITE_LIST_SEPARATOR));
                return;
            } else {
                guildName = args[0];
            }
        }

        Guild guild = GuildManager.getGuildFind(guildName);

        if (guild == null) {
            Message.CHAT_GUILD_NAMENOTEXIST.send(sender);
            return;
        }

        if (!nPlayer.isInvitedTo(guild) && !guild.isOpenInvitation()) {
            Message.CHAT_PLAYER_INVITE_NOTINVITED.send(sender);
            return;
        }

        Map<VarKey, String> vars = new HashMap<>();


        double joinMoney = GroupManager.getGroup(sender).get(GroupImpl.Key.JOIN_MONEY);

        if (joinMoney > 0 && !nPlayer.hasMoney(joinMoney)) {
            vars.put(VarKey.REQUIREDMONEY, String.valueOf(joinMoney));
            Message.CHAT_GUILD_NOTENOUGHMONEY.clone().vars(vars).send(sender);
            return;
        }

        if (joinMoney > 0) {
            nPlayer.takeMoney(joinMoney);
        }

        if (guild.isFull()) {
            Message.CHAT_GUILD_ISFULL.send(sender);
            return;
        }

        guild.addPlayer(nPlayer);
        nPlayer.deleteInvitation(guild);
        TagUtils.refresh();
        TabUtils.refresh();
        Message.CHAT_GUILD_JOINED.send(sender);
        guild.showVaultHologram(nPlayer.getPlayer());

        vars.clear();
        vars.put(VarKey.PLAYER_NAME, sender.getName());
        vars.put(VarKey.GUILD_NAME, guild.getName());
        Message.BROADCAST_GUILD_JOINED.clone().vars(vars).broadcast();
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        Set<String> options = new HashSet<>();
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        for (Guild guild : nPlayer.getInvitedTo()) {
            options.add(guild.getTag().toLowerCase());
            options.add(guild.getName().toLowerCase());
        }

        return options;
    }
}
