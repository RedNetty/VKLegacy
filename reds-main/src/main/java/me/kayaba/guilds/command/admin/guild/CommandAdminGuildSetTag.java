package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.command.guild.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminGuildSetTag extends AbstractCommandExecutor.Reversed<Guild> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Guild guild = getParameter();

        if (args.length == 0) {
            Message.CHAT_GUILD_ENTERTAG.send(sender);
            return;
        }

        String newTag = args[0];

        MessageWrapper validityMessage = CommandGuildCreate.validTag(newTag);
        if (validityMessage != null) {
            validityMessage.send(sender);
            return;
        }


        guild.setTag(newTag);

        TagUtils.refresh();
        TabUtils.refresh();

        Message.CHAT_ADMIN_GUILD_SET_TAG.clone().setVar(VarKey.TAG, newTag).send(sender);
    }
}
