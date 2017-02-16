package io.vawke.practice.guild.factory.command;

import io.vawke.practice.Game;
import io.vawke.practice.guild.Guild;
import io.vawke.practice.util.UUIDCallback;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.UUID;

/**
 * Created by Giovanni on 13-2-2017.
 */
public class GuildKickCommand extends GuildCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (Game.getGame().directGuildRegistry().hasGuild(player.getUniqueId())) {
                Guild guild = Game.getGame().directGuildRegistry().playerGuild(player);
                if (guild.getOfficers().contains(player.getUniqueId()) || guild.getOwnerUniqueId().equals(player.getUniqueId())) {
                    if (strings.length > 0) {
                        String target = strings[0];
                        if (target.equals(player.getName())) {
                            player.sendMessage(ChatColor.RED + "You cannot kick yourself!");
                            return true;
                        } else {
                            UUID callback = null;
                            try {
                                callback = new UUIDCallback(Collections.singletonList(target), Game.getPracticeServer().getServer().getScheduler()).call().get(target);
                            } catch (Exception e) {
                                player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + target + ChatColor.RED + " does not exist!");
                            }
                            if (callback != null) {
                                if (guild.getMembers().contains(callback)) {
                                    boolean owner = guild.getOwnerUniqueId().equals(callback);
                                    boolean officer = guild.getOfficers().contains(callback);
                                    if (owner) {
                                        player.sendMessage(ChatColor.RED + "You can't kick the owner of the guild!");
                                        return true;
                                    }
                                    if (officer) {
                                        player.sendMessage(ChatColor.RED + "You can't kick a guild officer!");
                                        return true;
                                    }
                                    guild.getMembers().remove(callback);
                                    StringBuilder stringBuilder = new StringBuilder(strings[1]);

                                    for (int arg = 1; arg < strings.length; arg++)
                                        stringBuilder.append(" ").append(strings[arg]);

                                    if (Bukkit.getPlayer(callback) != null && Bukkit.getPlayer(callback).isOnline()) {
                                        Player damned = Bukkit.getPlayer(callback);
                                        damned.sendMessage("");
                                        damned.sendMessage(ChatColor.RED + "You have been " + ChatColor.UNDERLINE + "kicked" + ChatColor.RED + " from " + guild.getName());
                                        damned.sendMessage(ChatColor.RED + "Reason: " + stringBuilder.toString());
                                    }
                                    player.sendMessage(ChatColor.RED + "Player kicked!");
                                } else {
                                    player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + target + ChatColor.RED + " is not in your guild");
                                    return true;
                                }
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /gkick <playerName> <reason>");
                        return true;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You must be at least a guild " + ChatColor.BOLD + "OFFICER" + ChatColor.RED + " to use " + ChatColor.BOLD + "/gkick");
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.RED + "You must be in a " + ChatColor.BOLD + "GUILD" + ChatColor.RED + " to use " + ChatColor.BOLD + "/gkick");
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerWatcher() {
        Game.getPracticeServer().getCommand("gkick").setExecutor(this);
    }
}
