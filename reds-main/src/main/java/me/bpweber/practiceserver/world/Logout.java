/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package me.bpweber.practiceserver.world;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.player.Listeners;
import me.bpweber.practiceserver.pvp.Alignments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Logout
        implements Listener,
        CommandExecutor {
    static HashMap<String, Integer> logging = new HashMap<String, Integer>();
    static HashMap<String, Location> loggingloc = new HashMap<String, Location>();
    static HashMap<String, Long> syncing = new HashMap<String, Long>();

    public void onEnable() {
        PracticeServer.log.info("[Logout] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (!Logout.logging.containsKey(p.getName())) continue;
                    if (Logout.logging.get(p.getName()) == 0) {
                        Logout.logging.remove(p.getName());
                        Logout.loggingloc.remove(p.getName());

                        if (Alignments.tagged.containsKey(p.getName())) {
                            Alignments.tagged.remove(p.getName());
                        }
                        if (Listeners.combat.containsKey(p.getName())) {
                            Alignments.tagged.remove(p.getName());
                        }
                        p.saveData();
                        p.kickPlayer(String.valueOf(ChatColor.GREEN.toString()) + "You have safely logged out." + "\n\n" + ChatColor.GRAY.toString() + "Your player data has been synced.");
                        continue;
                    }
                    p.sendMessage(ChatColor.RED + "Logging out in ... " + ChatColor.BOLD + Logout.logging.get(p.getName()) + "s");
                    Logout.logging.put(p.getName(), Logout.logging.get(p.getName()) - 1);
                }
            }
        }.runTaskTimer(PracticeServer.plugin, 10, 10);
    }

    public void onDisable() {
        PracticeServer.log.info("[Logout] has been disabled.");
    }

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("logout") && !logging.containsKey(p.getName())) {
                if (Alignments.isSafeZone(p.getLocation())) {
                    if (Alignments.tagged.containsKey(p.getName())) {
                        Alignments.tagged.remove(p.getName());
                    }
                    p.saveData();
                    p.kickPlayer(String.valueOf(ChatColor.GREEN.toString()) + "You have safely logged out." + "\n\n" + ChatColor.GRAY.toString() + "Your player data has been synced.");
                } else if (Alignments.tagged.containsKey(p.getName()) && System.currentTimeMillis() - Alignments.tagged.get(p.getName()) < 10000) {
                    p.sendMessage(ChatColor.RED + "You will be " + ChatColor.BOLD + "LOGGED OUT" + ChatColor.RED + " of the game world shortly.");
                    logging.put(p.getName(), 10);
                    loggingloc.put(p.getName(), p.getLocation());
                } else {
                    p.sendMessage(ChatColor.RED + "You will be " + ChatColor.BOLD + "LOGGED OUT" + ChatColor.RED + " of the game world shortly.");
                    logging.put(p.getName(), 3);
                    loggingloc.put(p.getName(), p.getLocation());
                }
            }
            if (cmd.getName().equalsIgnoreCase("sync")) {
                if (syncing.containsKey(p.getName()) && System.currentTimeMillis() - syncing.get(p.getName()) < 10000) {
                    p.sendMessage(ChatColor.RED + "You already have a recent sync request -- please wait a few seconds.");
                } else {
                    p.updateInventory();
                    p.teleport(p);
                    p.saveData();
                    p.sendMessage(ChatColor.GREEN + "Synced player data to " + ChatColor.UNDERLINE + "HIVE" + ChatColor.GREEN + " server.");
                    syncing.put(p.getName(), System.currentTimeMillis());
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onCancelDamager(EntityDamageByEntityEvent e) {
        Player p;
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity && logging.containsKey((p = (Player) e.getDamager()).getName())) {
            logging.remove(p.getName());
            loggingloc.remove(p.getName());
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Logout - CANCELLED");
        }
    }

    @EventHandler
    public void onCancelDamage(EntityDamageEvent e) {
        Player p;
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getEntity() instanceof Player && logging.containsKey((p = (Player) e.getEntity()).getName())) {
            logging.remove(p.getName());
            loggingloc.remove(p.getName());
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Logout - CANCELLED");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        p.saveData();
        if (logging.containsKey(p.getName())) {
            logging.remove(p.getName());
            loggingloc.remove(p.getName());
        }
    }

    @EventHandler
    public void onCancelMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (logging.containsKey(p.getName()) && (loggingloc.get(p.getName())).distanceSquared(e.getTo()) >= 2.0) {
            logging.remove(p.getName());
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Logout - CANCELLED");
        }
    }

}

