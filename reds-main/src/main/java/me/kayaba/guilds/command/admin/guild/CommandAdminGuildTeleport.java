package me.kayaba.guilds.command.admin.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class CommandAdminGuildTeleport extends AbstractCommandExecutor.Reversed<Guild> {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        Guild guild = getParameter();

        Location home = guild.getHome();

        Player player;
        boolean other = false;

        Map<VarKey, String> vars = new HashMap<>();
        vars.put(VarKey.GUILD_NAME, guild.getName());

        if (args.length == 1) {
            if (!Permission.GUILDS_ADMIN_GUILD_TELEPORT_OTHER.has(sender)) {
                Message.CHAT_NOPERMISSIONS.send(sender);
                return;
            }

            String playerName = args[0];
            GPlayer nPlayerOther = PlayerManager.getPlayer(playerName);

            if (nPlayerOther == null) {
                Message.CHAT_PLAYER_NOTEXISTS.send(sender);
                return;
            }

            if (!nPlayerOther.isOnline()) {
                Message.CHAT_PLAYER_NOTONLINE.send(sender);
                return;
            }

            player = nPlayerOther.getPlayer();
            other = true;
        } else {
            if (!(sender instanceof Player)) {
                Message.CHAT_CMDFROMCONSOLE.send(sender);
                return;
            }

            player = (Player) sender;
        }

        if (other) {
            vars.put(VarKey.PLAYER_NAME, player.getName());
            Message.CHAT_ADMIN_GUILD_TELEPORTED_OTHER.clone().vars(vars).send(sender);
        } else {
            Message.CHAT_ADMIN_GUILD_TELEPORTED_SELF.clone().vars(vars).send(sender);
        }

        player.teleport(home);
    }
}
