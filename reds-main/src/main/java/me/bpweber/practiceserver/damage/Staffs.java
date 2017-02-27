/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Snowball
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LargeFireball
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.SmallFireball
 *  org.bukkit.entity.Snowball
 *  org.bukkit.entity.WitherSkull
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockIgniteEvent
 *  org.bukkit.event.block.BlockIgniteEvent$IgniteCause
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityExplodeEvent
 *  org.bukkit.event.entity.ExplosionPrimeEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.util.Vector
 */
package me.bpweber.practiceserver.damage;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.player.Energy;
import me.bpweber.practiceserver.pvp.Alignments;
import me.bpweber.practiceserver.utils.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class Staffs
        implements Listener {
    HashMap<Projectile, ItemStack> shots = new HashMap<Projectile, ItemStack>();
    public static HashMap<Player, ItemStack> staff = new HashMap<Player, ItemStack>();
    ArrayList<UUID> canShoot = new ArrayList<UUID>();
    public PracticeServer m;

    public void onEnable() {
        PracticeServer.log.info("[Staffs] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
    }

    public void onDisable() {
        PracticeServer.log.info("[Staffs] has been disabled.");
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onStaffShot(PlayerInteractEvent e) {
        Player p;
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && (p = e.getPlayer()).getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR && p.getInventory().getItemInMainHand().getType().name().contains("_HOE") && p.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
            if (Alignments.isSafeZone(p.getLocation())) {
                p.playSound(p.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.25f);
                ParticleEffect.CRIT_MAGIC.display(0.0f, 0.0f, 0.0f, 0.5f, 20, p.getLocation().add(0.0, 1.0, 0.0), 20.0);
            } else {
                if (Energy.nodamage.containsKey(p.getName()) && System.currentTimeMillis() - Energy.nodamage.get(p.getName()) < 100) {
                    e.setCancelled(true);
                    return;
                }
                if(canShoot.contains(p.getUniqueId())) {
                    e.setCancelled(true);
                    return;
                }
                if (Energy.getEnergy(p) > 0.0f) {
                    int amt = 0;
                    Projectile ep = null;
                    if (p.getInventory().getItemInMainHand().getType() == Material.WOOD_HOE) {
                        ep = p.launchProjectile(Snowball.class);
                        this.shots.put(ep, p.getInventory().getItemInMainHand());
                        amt = 7;
                    }
                    if (p.getInventory().getItemInMainHand().getType() == Material.STONE_HOE) {
                        ep = p.launchProjectile(Egg.class);
                        ep.setVelocity(ep.getVelocity().multiply(1));
                        ep.setBounce(false);
                        this.shots.put(ep, p.getInventory().getItemInMainHand());
                        amt = 8;
                    }
                    if (p.getInventory().getItemInMainHand().getType() == Material.IRON_HOE) {
                        ep = p.launchProjectile(EnderPearl.class);
                        ep.setVelocity(ep.getVelocity().multiply(1));
                        this.shots.put(ep, p.getInventory().getItemInMainHand());
                        amt = 9;
                    }
                    if (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HOE) {
                        ep = p.launchProjectile(WitherSkull.class);
                        ep.setVelocity(ep.getVelocity().multiply(2));
                        this.shots.put(ep, p.getInventory().getItemInMainHand());
                        amt = 10;
                    }
                    if (p.getInventory().getItemInMainHand().getType() == Material.GOLD_HOE) {
                        ep = p.launchProjectile(SmallFireball.class);
                        ep.setVelocity(ep.getVelocity().multiply(1));
                        this.shots.put(ep, p.getInventory().getItemInMainHand());
                        amt = 11;
                    }
                    if (((new Random()).nextInt(2000)) <= p.getInventory().getItemInMainHand().getType().getMaxDurability()) {
                        if (p.getInventory().getItemInMainHand().getDurability() >= p.getInventory().getItemInMainHand().getType().getMaxDurability()) {
                            p.getInventory().setItemInMainHand(null);
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        } else {
                            p.getInventory().getItemInMainHand().setDurability((short) (p.getInventory().getItemInMainHand().getDurability() + 1));
                        }
                    }
                    Energy.removeEnergy(p, amt);
                    p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 0.25f);
                    this.shots.put(ep, p.getInventory().getItemInMainHand());
                    new BukkitRunnable() {

                        public void run() {
                            canShoot.remove(p.getUniqueId());
                        }
                    }.runTaskLaterAsynchronously(PracticeServer.plugin, 15);
                } else {
                    Energy.setEnergy(p, 0.0f);
                    Energy.cd.put(p.getName(), System.currentTimeMillis());
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 5), true);
                    p.playSound(p.getLocation(), Sound.ENTITY_WOLF_PANT, 10.0f, 1.5f);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(ProjectileHitEvent e) {
        Projectile pj = e.getEntity();
        if (pj.getShooter() != null && pj.getShooter() instanceof Player) {
            Player d = (Player) pj.getShooter();
            if (this.shots.containsKey(pj)) {
                LivingEntity target = null;
                ItemStack wep = this.shots.get(pj);
                this.shots.remove(pj);

                for (Entity ent : pj.getNearbyEntities(2.0, 2.0, 2.0)) {
                    if (!(ent instanceof LivingEntity) || ent == d) continue;
                    target = (LivingEntity) ent;
                }
                if (target != null) {
                    if (pj instanceof Snowball) {
                        e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_SNOW_BREAK, 1.0f, 1.0f);


                    }
                    staff.put(d, wep);
                    target.damage(1.0, d);
                    staff.remove(d);
                }
                this.shots.remove(pj);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockIgnite(BlockIgniteEvent e) {
        if (e.getCause() == BlockIgniteEvent.IgniteCause.FIREBALL) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityExplodePrimeEvent(ExplosionPrimeEvent e) {
        e.setFire(false);
        e.setRadius(0.0f);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityExplodeEvent(EntityExplodeEvent e) {
        e.setCancelled(true);
        e.setYield(0.0f);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile) {
            e.setCancelled(true);
            e.setDamage(0.0);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTp(PlayerTeleportEvent e) {
        if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            e.setCancelled(true);
        }
    }
}

