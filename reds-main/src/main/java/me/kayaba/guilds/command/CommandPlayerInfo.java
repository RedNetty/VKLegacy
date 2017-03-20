package me.kayaba.guilds.command;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class CommandPlayerInfo extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nCPlayer;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                Message.CHAT_CMDFROMCONSOLE.send(sender);
                return;
            }

            nCPlayer = PlayerManager.getPlayer(sender);
        } else {
            nCPlayer = PlayerManager.getPlayer(args[0]);

            if (nCPlayer == null) {
                Message.CHAT_PLAYER_NOTEXISTS.send(sender);
                return;
            }
        }

        plugin.getPlayerManager().sendPlayerInfo(sender, nCPlayer);
    }
}
