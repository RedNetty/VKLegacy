package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandGuildOpenInvitation extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        if (!nPlayer.hasPermission(GuildPermission.OPENINVITATION)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        final boolean status = !nPlayer.getGuild().isOpenInvitation();
        nPlayer.getGuild().setOpenInvitation(status);

        Message.CHAT_GUILD_OPENINVITATION.clone().setVar(VarKey.STATUS, Message.getOnOff(status)).send(sender);

        TabUtils.refresh(nPlayer.getGuild());
    }
}
