/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package me.bpweber.practiceserver.player;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.damage.Damage;
import me.bpweber.practiceserver.pvp.Alignments;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Energy
        implements Listener {
    public static HashMap<String, Long> nodamage = new HashMap<String, Long>();
    public static HashMap<String, Long> cd = new HashMap<String, Long>();

    public void onEnable() {
        PracticeServer.log.info("[Energy] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    PlayerInventory i = p.getInventory();
                    float amt = 100.0f;
                    ItemStack[] arritemStack = i.getArmorContents();
                    int n = arritemStack.length;
                    int n2 = 0;
                    while (n2 < n) {
                        ItemStack is = arritemStack[n2];
                        if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                            int added = Damage.getEnergy(is);
                            int intel = 0;
                            int addedint = Damage.getElem(is, "INT");
                            if ((intel += addedint) > 0) {
                                added = (int) ((long) added + Math.round((double) intel * 0.009));
                            }
                            amt += (float) (added * 5);
                        }
                        ++n2;
                    }
                    if (Energy.getEnergy(p) < 100.0f && (!Energy.cd.containsKey(p.getName()) || Energy.cd.containsKey(p.getName()) && System.currentTimeMillis() - Energy.cd.get(p.getName()) > 2000)) {
                        Energy.setEnergy(p, Energy.getEnergy(p) + amt / 100.0f);
                    }
                    if (Energy.getEnergy(p) > 0.0f) continue;
                    p.setSprinting(false);
                }
            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 1, 1);
        new BukkitRunnable() {

            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (!p.isSprinting() || Alignments.isSafeZone(p.getLocation())) continue;
                    float amt = 100.0f;
                    amt += 85.0f;
                    amt *= 4.0f;
                    if (Energy.getEnergy(p) > 0.0f) {
                        Energy.setEnergy(p, Energy.getEnergy(p) - amt / 100.0f);
                    }
                    if (Energy.getEnergy(p) > 0.0f) continue;
                    Energy.setEnergy(p, 0.0f);
                    Energy.cd.put(p.getName(), System.currentTimeMillis());
                    slowDig(p);
                }
            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 4, 4);
    }

    public void slowDig(Player player) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.plugin, () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 5), true);
            player.setSprinting(false);
        });
    }

    public void onDisable() {
        PracticeServer.log.info("[Energy] has been disabled.");
    }

    public static float getEnergy(Player p) {
        float energy = 0.0f;
        energy = p.getExp() * 100.0f;
        return energy;
    }

    public static void setEnergy(Player p, float energy) {
        if (energy > 100.0f) {
            energy = 100.0f;
        }
        p.setExp(energy / 100.0f);
        p.setLevel((int) energy);
    }

    public static void removeEnergy(Player p, int amt) {
        if (Alignments.isSafeZone(p.getLocation())) {
            return;
        }
        if (p.hasMetadata("lastenergy") && System.currentTimeMillis() - ((MetadataValue) p.getMetadata("lastenergy").get(0)).asLong() < 100) {
            return;
        }
        p.setMetadata("lastenergy", (MetadataValue) new FixedMetadataValue(PracticeServer.plugin, (Object) System.currentTimeMillis()));
        Energy.setEnergy(p, Energy.getEnergy(p) - (float) amt);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnergyUse(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            if (nodamage.containsKey(p.getName()) && System.currentTimeMillis() - nodamage.get(p.getName()) < 100) {
                e.setUseItemInHand(Event.Result.DENY);
                e.setCancelled(true);
                return;
            }
            if (Energy.getEnergy(p) > 0.0f) {
                int amt = 6;
                if (p.getInventory().getItemInMainHand().getType() == Material.WOOD_SWORD) {
                    amt = 5;
                } else if (p.getInventory().getItemInMainHand().getType() == Material.WOOD_AXE || p.getInventory().getItemInMainHand().getType() == Material.WOOD_SPADE || p.getInventory().getItemInMainHand().getType() == Material.STONE_SWORD) {
                    amt = 6;
                } else if (p.getInventory().getItemInMainHand().getType() == Material.STONE_AXE || p.getInventory().getItemInMainHand().getType() == Material.STONE_SPADE || p.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD) {
                    amt = 7;
                } else if (p.getInventory().getItemInMainHand().getType() == Material.IRON_AXE || p.getInventory().getItemInMainHand().getType() == Material.IRON_SPADE || p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD) {
                    amt = 8;
                } else if (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE || p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SPADE || p.getInventory().getItemInMainHand().getType() == Material.GOLD_SWORD) {
                    amt = 8;
                } else if (p.getInventory().getItemInMainHand().getType() == Material.GOLD_AXE || p.getInventory().getItemInMainHand().getType() == Material.GOLD_SPADE) {
                    amt = 9;
                }
                Energy.removeEnergy(p, amt);
            }
            if (Energy.getEnergy(p) <= 0.0f) {
                Energy.setEnergy(p, 0.0f);
                cd.put(p.getName(), System.currentTimeMillis());
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 5), true);
                p.playSound(p.getLocation(), Sound.ENTITY_WOLF_PANT, 10.0f, 1.5f);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnergyUseDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
            Player p = (Player) e.getDamager();
            if (nodamage.containsKey(p.getName()) && System.currentTimeMillis() - nodamage.get(p.getName()) < 100) {
                e.setCancelled(true);
                e.setDamage(0.0);
                return;
            }
            nodamage.put(p.getName(), System.currentTimeMillis());
            if (Energy.getEnergy(p) > 0.0f) {
                if (p.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                    p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                }
                int amt = 6;
                if (p.getInventory().getItemInMainHand().getType() == Material.WOOD_HOE || p.getInventory().getItemInMainHand().getType() == Material.WOOD_SWORD) {
                    amt = 7;
                } else if (p.getInventory().getItemInMainHand().getType() == Material.STONE_HOE || p.getInventory().getItemInMainHand().getType() == Material.WOOD_AXE || p.getInventory().getItemInMainHand().getType() == Material.STONE_SWORD) {
                    amt = 8;
                } else if (p.getInventory().getItemInMainHand().getType() == Material.IRON_HOE ||p.getInventory().getItemInMainHand().getType() == Material.STONE_AXE || p.getInventory().getItemInMainHand().getType() == Material.IRON_SWORD) {
                    amt = 9;
                } else if (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HOE ||p.getInventory().getItemInMainHand().getType() == Material.IRON_AXE || p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD) {
                    amt = 10;
                } else if (p.getInventory().getItemInMainHand().getType() == Material.GOLD_HOE ||p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_AXE || p.getInventory().getItemInMainHand().getType() == Material.GOLD_SWORD) {
                    amt = 11;
                } else if (p.getInventory().getItemInMainHand().getType() == Material.GOLD_AXE) {
                    amt = 12;
                }
                Energy.removeEnergy(p, amt);
            }
            if (Energy.getEnergy(p) <= 0.0f) {
                Energy.setEnergy(p, 0.0f);
                cd.put(p.getName(), System.currentTimeMillis());
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 5), true);
                p.playSound(p.getLocation(), Sound.ENTITY_WOLF_PANT, 10.0f, 1.5f);
            }
        }
    }

}

