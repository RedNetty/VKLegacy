package me.bpweber.practiceserver.ModerationMechanics.Commands;

import de.Herbystar.TTA.TTA_Methods;
import me.bpweber.practiceserver.PracticeServer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Created by jaxon on 3/12/2017.
 */
public class Reboot implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reboot")) {
            if (sender.isOp() || sender instanceof ConsoleCommandSender) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "60s" + ChatColor.RED + "..");
                }
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "30s" + ChatColor.RED + "..");
                        }
                    }
                }, 600);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "10s" + ChatColor.RED + "..");
                        }
                    }
                }, 1000);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {

                            player.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "5s" + ChatColor.RED + "..");

                        }
                    }
                }, 1100);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "4s" + ChatColor.RED + "..");
                        }
                    }
                }, 1120);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "3s" + ChatColor.RED + "..");
                        }
                    }
                }, 1140);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "2s" + ChatColor.RED + "..");
                        }
                    }
                }, 1180);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendMessage(ChatColor.RED + "Rebooting in " + ChatColor.BOLD.toString() + ChatColor.UNDERLINE + "1s" + ChatColor.RED + "..");
                            TTA_Methods.sendTitle(player, ChatColor.RED + "Rebooting..", 20, 20, 20, ChatColor.GRAY + "Join back in a few moments!", 20, 20, 20);
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart");
                        }
                    }
                }, 1200);


            }
        }
        return false;
    }
}