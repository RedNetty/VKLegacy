package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandAdminGuildInvite extends AbstractCommandExecutor.Reversed<Guild> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Guild guild = getParameter();

        if (args.length == 0) {
            Message.CHAT_PLAYER_ENTERNAME.send(sender);
            return;
        }

        GPlayer nPlayer = PlayerManager.getPlayer(args[0]);

        if (nPlayer == null) {
            Message.CHAT_PLAYER_NOTEXISTS.send(sender);
            return;
        }

        if (nPlayer.hasGuild()) {
            Message.CHAT_PLAYER_HASGUILD.send(sender);
            return;
        }

        if (nPlayer.isInvitedTo(guild)) {
            Message.CHAT_PLAYER_ALREADYINVITED.send(sender);
            return;
        }


        nPlayer.addInvitation(guild);

        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.PLAYER_NAME, nPlayer.getName());
        vars.put(VarKey.GUILD_NAME, guild.getName());
        Message.CHAT_ADMIN_GUILD_INVITED.clone().vars(vars).send(sender);

        if (nPlayer.isOnline()) {
            Message.CHAT_PLAYER_INVITE_NOTIFY.clone().vars(vars).send(nPlayer);
        }
    }
}
