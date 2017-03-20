package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class CommandGuildInvite extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        if (args.length != 1) {
            getCommand().getUsageMessage().send(sender);
            return;
        }

        String playerName = args[0];
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        if (!nPlayer.hasPermission(GuildPermission.INVITE)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        GPlayer invitePlayer = PlayerManager.getPlayer(playerName);

        if (invitePlayer == null) {
            Message.CHAT_PLAYER_NOTEXISTS.send(sender);
            return;
        }

        if (invitePlayer.hasGuild()) {
            Message.CHAT_PLAYER_HASGUILD.send(sender);
            return;
        }

        Guild guild = nPlayer.getGuild();
        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.GUILD_NAME, guild.getName());
        vars.put(VarKey.PLAYER_NAME, invitePlayer.getName());

        if (!invitePlayer.isInvitedTo(guild)) {
            invitePlayer.addInvitation(guild);
            Message.CHAT_PLAYER_INVITE_INVITED.clone().vars(vars).send(sender);

            if (invitePlayer.isOnline()) {
                Message.CHAT_PLAYER_INVITE_NOTIFY.clone().vars(vars).send(invitePlayer.getPlayer());
            }
        } else {
            invitePlayer.deleteInvitation(guild);
            Message.CHAT_PLAYER_INVITE_CANCEL_SUCCESS.clone().vars(vars).send(sender);

            if (invitePlayer.isOnline()) {
                Message.CHAT_PLAYER_INVITE_CANCEL_NOTIFY.clone().vars(vars).send(invitePlayer.getPlayer());
            }
        }
    }

    @Override
    protected Collection<String> tabCompleteOptions(CommandSender sender, String[] args) {
        Set<String> options = new HashSet<>();

        for (Player player : CompatibilityUtils.getOnlinePlayers()) {
            options.add(player.getName());
        }

        return options;
    }
}
