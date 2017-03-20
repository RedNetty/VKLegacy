package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.GroupImpl.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandGuildBuySlot extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        if (!nPlayer.hasPermission(GuildPermission.BUYSLOT)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        if (nPlayer.getGuild().getSlots() + 1 > Config.GUILD_SLOTS_MAX.getInt()) {
            Message.CHAT_GUILD_BUY_SLOT_MAXREACHED.send(sender);
            return;
        }

        Group group = GroupManager.getGroup(sender);
        double money = GroupManager.getGroup("default").get(Key.BUY_SLOT_MONEY);

        if (money > 0 && !nPlayer.getGuild().hasMoney(money)) {
            Message.CHAT_GUILD_NOTENOUGHMONEY.send(sender);
            return;
        }

        nPlayer.getGuild().takeMoney(money);
        nPlayer.getGuild().addSlot();
        Message.CHAT_GUILD_BUY_SLOT_SUCCESS.send(sender);
        TabUtils.refresh(nPlayer.getGuild());
    }
}
