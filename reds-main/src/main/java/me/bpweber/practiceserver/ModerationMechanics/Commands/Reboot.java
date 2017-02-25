package me.bpweber.practiceserver.ModerationMechanics.Commands;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.player.Listeners;
import me.bpweber.practiceserver.pvp.Alignments;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;


/**
 * Created by jaxon on 2/23/2017.
 */
public class Reboot implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reboot") && sender.isOp()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD + "60s" + ChatColor.RED + "..");
            }
            new BukkitRunnable() {

                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD + "30s" + ChatColor.RED + "..");
                    }
                }
            }.runTaskLater(PracticeServer.plugin, 600);
            new BukkitRunnable() {

                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD + "10s" + ChatColor.RED + "..");
                    }
                }
            }.runTaskLater(PracticeServer.plugin, 1000);
            new BukkitRunnable() {

                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD + "5s" + ChatColor.RED + "..");
                    }
                }
            }.runTaskLater(PracticeServer.plugin, 1100);
            new BukkitRunnable() {

                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD + "4s" + ChatColor.RED + "..");
                    }
                }
            }.runTaskLater(PracticeServer.plugin, 1120);
            new BukkitRunnable() {

                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD + "3s" + ChatColor.RED + "..");
                    }
                }
            }.runTaskLater(PracticeServer.plugin, 1140);
            new BukkitRunnable() {

                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD + "2s" + ChatColor.RED + "..");
                    }
                }
            }.runTaskLater(PracticeServer.plugin, 1160);
            new BukkitRunnable() {

                public void run() {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD + "1s" + ChatColor.RED + "..");
                        Alignments.tagged.remove(p);
                        Listeners.combat.remove(p);
                        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                        Bukkit.dispatchCommand(console, "restart");
                    }
                }
            }.runTaskLater(PracticeServer.plugin, 1180);


        }
        return false;
    }

}