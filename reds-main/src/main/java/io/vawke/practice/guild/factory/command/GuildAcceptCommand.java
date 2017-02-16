package io.vawke.practice.guild.factory.command;

import io.vawke.practice.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Giovanni on 13-2-2017.
 */
public class GuildAcceptCommand extends GuildCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            boolean hasInvitation = Game.getGame().directGuildRegistry().hasInvitation(player.getUniqueId());
            if (hasInvitation) {
                Game.getGame().directGuildRegistry().getInvitedGuildOf(player.getUniqueId()).acceptJoin(player);
            } else {
                player.sendMessage(ChatColor.RED + "No pending guild invitation");
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerWatcher() {
        Game.getPracticeServer().getCommand("gaccept").setExecutor(this);
    }
}
