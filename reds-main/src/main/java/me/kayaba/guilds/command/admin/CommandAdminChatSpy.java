package me.kayaba.guilds.command.admin;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.command.*;

import java.util.*;

public class CommandAdminChatSpy extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);
        GPlayer nPlayerChange;

        if (args.length == 1) {
            if (!Permission.GUILDS_ADMIN_CHATSPY_OTHER.has(sender)) {
                Message.CHAT_NOPERMISSIONS.send(sender);
                return;
            }

            nPlayerChange = PlayerManager.getPlayer(args[0]);

            if (nPlayerChange == null) {
                Message.CHAT_PLAYER_NOTEXISTS.send(sender);
                return;
            }
        } else {
            nPlayerChange = nPlayer;
        }

        nPlayerChange.getPreferences().toggleSpyMode();
        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.MODE, Message.getOnOff(nPlayerChange.getPreferences().getSpyMode()));


        if (!nPlayer.equals(nPlayerChange)) {
            vars.put(VarKey.PLAYER_NAME, nPlayerChange.getName());

            Message.CHAT_ADMIN_SPYMODE_NOTIFY.clone().vars(vars).send(nPlayerChange);
            Message.CHAT_ADMIN_SPYMODE_SUCCESS_OTHER.clone().vars(vars).send(sender);
            return;
        }

        Message.CHAT_ADMIN_SPYMODE_SUCCESS_SELF.clone().vars(vars).send(sender);
    }
}
