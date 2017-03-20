package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandGuildSetTag extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        if (!nPlayer.hasPermission(GuildPermission.SET_TAG)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        if (args.length == 0) {
            Message.CHAT_GUILD_ENTERTAG.send(sender);
            return;
        }

        String newTag = args[0];
        newTag = StringUtils.removeColors(newTag);
        MessageWrapper validity = CommandGuildCreate.validTag(newTag);

        if (validity != null) {
            validity.send(sender);
            return;
        }

        nPlayer.getGuild().setTag(newTag);
        Message.CHAT_GUILD_SET_TAG.send(sender);
        TabUtils.refresh(nPlayer.getGuild());
        TagUtils.refresh(nPlayer.getGuild());
    }
}
