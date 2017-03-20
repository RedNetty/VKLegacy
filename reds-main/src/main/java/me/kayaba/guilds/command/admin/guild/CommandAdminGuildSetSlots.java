package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandAdminGuildSetSlots extends AbstractCommandExecutor.Reversed<Guild> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Guild guild = getParameter();

        if (args.length != 1) {
            getCommand().getUsageMessage().send(sender);
            return;
        }

        if (!NumberUtils.isNumeric(args[0])) {
            Message.CHAT_ENTERINTEGER.send(sender);
            return;
        }

        int slots = Integer.parseInt(args[0]);

        if (slots <= 0) {
            Message.CHAT_BASIC_NEGATIVENUMBER.send(sender);
            return;
        }

        if (slots < guild.getPlayers().size()) {
            Message.CHAT_ADMIN_GUILD_SET_SLOTS_SMALLERTHANPLAYERS.send(sender);
            return;
        }

        if (slots > Config.GUILD_SLOTS_MAX.getInt()) {
            Message.CHAT_MAXAMOUNT.clone().setVar(VarKey.AMOUNT, Config.GUILD_SLOTS_MAX.getInt()).send(sender);
            return;
        }

        guild.setSlots(slots);
        TabUtils.refresh(guild);

        Message.CHAT_ADMIN_GUILD_SET_SLOTS_SUCCESS.send(sender);
    }
}
