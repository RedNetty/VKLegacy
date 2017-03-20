package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandGuildChatMode extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        ChatMode chatMode;
        if (args.length == 0) {
            chatMode = nPlayer.getPreferences().getChatMode().next();
        } else {
            chatMode = ChatMode.fromString(args[0]);
        }

        if (chatMode == null) {
            Message.CHAT_GUILD_CHATMODE_INVALID.send(sender);
            return;
        }

        if (ChatMode.NORMAL.next() == ChatMode.NORMAL || !chatMode.isEnabled()) {
            Message.CHAT_NOPERMISSIONS.send(sender);
            return;
        }

        nPlayer.getPreferences().setChatMode(chatMode);

        Message.CHAT_GUILD_CHATMODE_SUCCESS.clone().setVar(VarKey.MODE, chatMode.getName().get()).send(sender);
        TabUtils.refresh(nPlayer);
    }

    @Override
    public Set<String> tabCompleteOptions(CommandSender sender, String[] args) {
        Set<String> options = new HashSet<>();

        for (ChatMode chatMode : ChatMode.valuesEnabled()) {
            options.add(chatMode.name().toLowerCase());
        }

        return options;
    }
}
