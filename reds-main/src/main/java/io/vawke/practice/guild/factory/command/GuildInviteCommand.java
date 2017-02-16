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
public class GuildInviteCommand extends GuildCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (Game.getGame().directGuildRegistry().hasGuild(player.getUniqueId())) {
                Guild guild = Game.getGame().directGuildRegistry().playerGuild(player);
                if (guild.getOfficers().contains(player.getUniqueId()) || guild.getOwnerUniqueId().equals(player.getUniqueId())) {
                    String target = strings[0];
                    if (target.equals(player.getName())) {
                        player.sendMessage(ChatColor.RED + "You cannot invite yourself!");
                        return true;
                    } else {
                        Player targetPlayer = null;
                        try {
                            targetPlayer = Bukkit.getPlayer(target);
                        } catch (Exception e) {
                            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + target + ChatColor.RED + " is not online!");
                        }
                        if (targetPlayer != null) {
                            if (guild.tryInvite(player, targetPlayer)) {
                                guild.getInvitationCache().put(targetPlayer.getUniqueId(), player.getName());
                                player.sendMessage(ChatColor.GRAY + "You have invited " + ChatColor.BOLD.toString() + ChatColor.DARK_AQUA + target + ChatColor.GRAY + " to join your guild!");
                                targetPlayer.sendMessage("");
                                targetPlayer.sendMessage(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + player.getName()
                                        + ChatColor.GRAY + " has invited you to join their guild, " + ChatColor.DARK_AQUA
                                        + guild.getName()
                                        + ChatColor.GRAY + ". To accept, type " + ChatColor.DARK_AQUA.toString() + "/gaccept"
                                        + ChatColor.GRAY + " to decline, type " + ChatColor.DARK_AQUA.toString() + "/gdecline");
                                targetPlayer.sendMessage("");
                            }
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You must be at least a guild " + ChatColor.BOLD + "OFFICER" + ChatColor.RED + " to use " + ChatColor.BOLD + "/ginvite");
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.RED + "You must be in a " + ChatColor.BOLD + "GUILD" + ChatColor.RED + " to use " + ChatColor.BOLD + "/ginvite");
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerWatcher() {
        Game.getPracticeServer().getCommand("ginvite").setExecutor(this);
    }
}
