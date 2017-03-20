package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

public class CommandGuildPvpToggle extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        if (!nPlayer.hasPermission(GuildPermission.PVPTOGGLE)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }

        nPlayer.getGuild().setFriendlyPvp(!nPlayer.getGuild().getFriendlyPvp());
        TabUtils.refresh(nPlayer.getGuild());

        Message.CHAT_GUILD_FPVPTOGGLED.clone().setVar(VarKey.GUILD_PVP, Message.getOnOff(nPlayer.getGuild().getFriendlyPvp())).send(sender);
    }
}
