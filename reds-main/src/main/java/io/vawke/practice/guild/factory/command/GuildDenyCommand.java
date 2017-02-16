package io.vawke.practice.guild.factory.command;

import io.vawke.practice.Game;
import io.vawke.practice.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Giovanni on 13-2-2017.
 */
public class GuildDenyCommand extends GuildCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (Game.getGame().directGuildRegistry().hasInvitation(player.getUniqueId())) {
                Guild guild = Game.getGame().directGuildRegistry().getInvitedGuildOf(player.getUniqueId());
                guild.getInvitationCache().remove(player.getUniqueId());
                player.sendMessage(ChatColor.RED + "Declined the invitation from '" + ChatColor.BOLD + guild.getName() + "'");

                String senderName = guild.getInvitationCache().get(player.getUniqueId());
                Player sender = Bukkit.getPlayer(senderName);
                if (sender != null && sender.isOnline()) {
                    sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + player.getName() + ChatColor.RED.toString() + " has DECLINED your guild invitation!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "No pending guild invitation :c!");
                return false;
            }
        }
        return false;
    }

    @Override
    public void registerWatcher() {
        Game.getPracticeServer().getCommand("gdeny").setExecutor(this);
    }
}
