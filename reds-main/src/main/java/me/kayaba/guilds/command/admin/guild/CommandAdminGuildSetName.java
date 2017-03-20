package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminGuildSetName extends AbstractCommandExecutor.Reversed<Guild> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Guild guild = getParameter();

        if (args.length == 0) {
            Message.CHAT_ADMIN_GUILD_SET_NAME_ENTERNEWNAME.send(sender);
            return;
        }

        String newName = args[0];

        if (newName.length() < Config.GUILD_SETTINGS_NAME_MIN.getInt()) {
            Message.CHAT_CREATEGUILD_NAME_TOOSHORT.send(sender);
            return;
        }

        if (newName.length() > Config.GUILD_SETTINGS_NAME_MAX.getInt()) {
            Message.CHAT_CREATEGUILD_NAME_TOOLONG.send(sender);
            return;
        }

        if (plugin.getGuildManager().exists(newName)) {
            Message.CHAT_CREATEGUILD_NAMEEXISTS.send(sender);
            return;
        }

        plugin.getGuildManager().changeName(guild, newName);
        TabUtils.refresh(guild);

        Message.CHAT_ADMIN_GUILD_SET_NAME_SUCCESS.send(sender);
    }
}
