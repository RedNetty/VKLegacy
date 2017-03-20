package me.bpweber.practiceserver.teleport;

import me.bpweber.practiceserver.*;
import me.bpweber.practiceserver.player.*;
import me.bpweber.practiceserver.pvp.*;
import me.bpweber.practiceserver.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.scheduler.*;

import java.util.*;

public class Hearthstone
        implements Listener {
    static HashMap<String, Integer> casting = new HashMap<String, Integer>();
    static HashMap<String, Location> castingloc = new HashMap<String, Location>();

    public void onEnable() {
        PracticeServer.log.info("[Hearthstone] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (!p.isOnline() || !Hearthstone.casting.containsKey(p.getName())) continue;
                    if (Hearthstone.casting.get(p.getName()) == 0) {
                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);
                        Hearthstone.casting.remove(p.getName());
                        Hearthstone.castingloc.remove(p.getName());
                        p.eject();
                        p.teleport(TeleportBooks.Cyrennica);
                        continue;
                    }
                    Particles.SPELL.display(0.0f, 0.0f, 0.0f, 0.5f, 80, p.getLocation().add(0.0, 0.15, 0.0), 20.0);
                    p.sendMessage(ChatColor.BOLD + "TELEPORTING" + ChatColor.WHITE + " ... " + Hearthstone.casting.get(p.getName()) + "s");
                    Hearthstone.casting.put(p.getName(), Hearthstone.casting.get(p.getName()) - 1);
                }
            }
        }.runTaskTimer(PracticeServer.plugin, 20, 20);
    }

    public void onDisable() {
        PracticeServer.log.info("[Hearthstone] has been disabled.");
    }

    public static ItemStack hearthstone() {
        ItemStack is = new ItemStack(Material.QUARTZ);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Hearthstone");
        im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports you to your home town.", ChatColor.GRAY + "Talk to an Innkeeper to change your home town.", ChatColor.GREEN + "Location: Cyrennica"));
        is.setItemMeta(im);
        return is;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK || p.getInventory().getItemInMainHand() == null || !p.getInventory().getItemInMainHand().equals(Hearthstone.hearthstone()) || casting.containsKey(p.getName()) || Horses.mounting.containsKey(p.getName()))) {
            if (Alignments.chaotic.containsKey(p.getName())) {
                p.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " do this while chaotic!");
            } else {
                p.sendMessage(ChatColor.BOLD + "TELEPORTING" + ChatColor.WHITE + " - " + ChatColor.AQUA + "Cyrennica" + ChatColor.WHITE + " ... " + 10 + "s");
                casting.put(p.getName(), 10);
                castingloc.put(p.getName(), p.getLocation());
            }
        }
    }

    @EventHandler
    public void onCancelDamager(EntityDamageByEntityEvent e) {
        Player p;
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity && casting.containsKey((p = (Player) e.getDamager()).getName())) {
            casting.remove(p.getName());
            castingloc.remove(p.getName());
            p.sendMessage(ChatColor.RED + "Hearthstone - " + ChatColor.BOLD + "CANCELLED");
        }
    }

    @EventHandler
    public void onCancelDamage(EntityDamageEvent e) {
        Player p;
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getEntity() instanceof Player && casting.containsKey((p = (Player) e.getEntity()).getName())) {
            casting.remove(p.getName());
            castingloc.remove(p.getName());
            p.sendMessage(ChatColor.RED + "Hearthstone - " + ChatColor.BOLD + "CANCELLED");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (casting.containsKey(p.getName())) {
            casting.remove(p.getName());
            castingloc.remove(p.getName());
        }
    }

    @EventHandler
    public void onCancelMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (casting.containsKey(p.getName()) && (castingloc.get(p.getName())).distanceSquared(e.getTo()) >= 2.0) {
            casting.remove(p.getName());
            p.sendMessage(ChatColor.RED + "Hearthstone - " + ChatColor.BOLD + "CANCELLED");
        }
    }

}

