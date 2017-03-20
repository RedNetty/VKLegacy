package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandGuildSetName extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        if (!nPlayer.hasPermission(GuildPermission.SET_NAME)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        if (args.length == 0) {
            Message.CHAT_GUILD_ENTERNAME.send(sender);
            return;
        }

        String newName = args[0];

        MessageWrapper validity = CommandGuildCreate.validName(newName);

        if (validity != null) {
            validity.send(sender);
            return;
        }

        plugin.getGuildManager().changeName(nPlayer.getGuild(), newName);
        Message.CHAT_GUILD_SET_NAME.send(sender);
        TabUtils.refresh(nPlayer.getGuild());
    }
}
