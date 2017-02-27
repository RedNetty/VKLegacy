/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.CaveSpider
 *  org.bukkit.entity.Creature
 *  org.bukkit.entity.EnderPearl
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LargeFireball
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.MagmaCube
 *  org.bukkit.entity.PigZombie
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.entity.Skeleton
 *  org.bukkit.entity.SmallFireball
 *  org.bukkit.entity.Snowball
 *  org.bukkit.entity.Spider
 *  org.bukkit.entity.WitherSkull
 *  org.bukkit.entity.Zombie
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.CreatureSpawnEvent
 *  org.bukkit.event.entity.EntityCombustEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityTargetEvent
 *  org.bukkit.event.entity.EntityTargetEvent$TargetReason
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.entity.SlimeSplitEvent
 *  org.bukkit.inventory.EntityEquipment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.Vector
 */
package me.bpweber.practiceserver.mobs;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.damage.Damage;
import me.bpweber.practiceserver.player.Listeners;
import me.bpweber.practiceserver.utils.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Mobs
        implements Listener {
    public static HashMap<LivingEntity, Integer> crit = new HashMap<LivingEntity, Integer>();
    static ConcurrentHashMap<Creature, Player> target = new ConcurrentHashMap<Creature, Player>();
    public static HashMap<UUID, Long> sound = new HashMap<UUID, Long>();

    public void onEnable() {
        PracticeServer.log.info("[Mobs] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                for (Entity ent : Bukkit.getWorlds().get(0).getEntities()) {
                    if (!(ent instanceof LivingEntity) || ent instanceof Player) continue;
                    LivingEntity l = (LivingEntity) ent;
                    if (Mobs.crit.containsKey(l) && Mobs.isElite(l)) {
                        int step = Mobs.crit.get(l);
                        if (step > 0) {
                            Mobs.crit.put(l, --step);
                            l.getWorld().playSound(l.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 4.0f);
                            ParticleEffect.EXPLOSION_LARGE.display(0.0f, 0.0f, 0.0f, 0.3f, 40, l.getLocation().add(0.0, 1.0, 0.0), 20.0);
                        }
                        if (step == 0) {
                            for (Entity e : l.getNearbyEntities(8.0, 8.0, 8.0)) {
                                if (!(e instanceof Player)) continue;
                                if (Listeners.mobd.containsKey(l.getUniqueId())) {
                                    Listeners.mobd.remove(l.getUniqueId());
                                }
                                Player p = (Player) e;
                                p.damage(l.getLastDamage(), l);
                                Vector v = p.getLocation().toVector().subtract(l.getLocation().toVector());
                                if (v.getX() != 0.0 || v.getY() != 0.0 || v.getZ() != 0.0) {
                                    v.normalize();
                                }
                                p.setVelocity(v.multiply(3));
                            }
                            l.getWorld().playSound(l.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.5f);
                            ParticleEffect.EXPLOSION_HUGE.display(0.0f, 0.0f, 0.0f, 1.0f, 40, l.getLocation().add(0.0, 1.0, 0.0), 20.0);
                            Mobs.crit.remove(l);
                            l.setCustomName(Mobs.generateOverheadBar(l, l.getHealth(), l.getMaxHealth(), Mobs.getMobTier(l), true));
                            l.setCustomNameVisible(true);
                            if (l.hasPotionEffect(PotionEffectType.SLOW)) {
                                l.removePotionEffect(PotionEffectType.SLOW);
                                if (l.getEquipment().getItemInMainHand() != null && l.getEquipment().getItemInMainHand().getType().name().contains("_HOE")) {
                                    l.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 3), true);
                                }
                            }
                            if (l.hasPotionEffect(PotionEffectType.JUMP)) {
                                l.removePotionEffect(PotionEffectType.JUMP);
                            }
                        }
                    }
                    if (!Listeners.named.containsKey(l.getUniqueId()) || System.currentTimeMillis() - Listeners.named.get(l.getUniqueId()) < 5000)
                        continue;
                    Listeners.named.remove(l.getUniqueId());
                    String name = "";
                    if (l.hasMetadata("name")) {
                        name = l.getMetadata("name").get(0).asString();
                    }
                    l.setCustomName(name);
                }
            }
        }.runTaskTimer(PracticeServer.plugin, 20, 20);
        new BukkitRunnable() {

            public void run() {
                for (Entity ent : Bukkit.getWorlds().get(0).getEntities()) {
                    LivingEntity l;
                    if (!(ent instanceof LivingEntity) || ent instanceof Player || !Mobs.crit.containsKey(l = (LivingEntity) ent) || Mobs.isElite(l))
                        continue;
                    int step = Mobs.crit.get(l);
                    if (step > 0) {
                        Mobs.crit.put(l, --step);
                        l.getWorld().playSound(l.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1.0f, 2.0f);
                    }
                    if (step != 0) continue;
                    ParticleEffect.SPELL_WITCH.display(0.0f, 0.0f, 0.0f, 0.5f, 35, l.getLocation().add(0.0, 1.0, 0.0), 20.0);
                }
            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 20, 10);
        new BukkitRunnable() {

            public void run() {
                for (Entity ent : Bukkit.getWorlds().get(0).getEntities()) {
                    Creature c;
                    if (!(ent instanceof Creature) || (c = (Creature) ent).getEquipment().getItemInMainHand() == null || !c.getEquipment().getItemInMainHand().getType().name().contains("_HOE"))
                        continue;
                    if (Mobs.isElite(c) && Mobs.crit.containsKey(c)) {
                        return;
                    }
                    if (!Mobs.isPlayerNearby(c) || c.getTarget() == null) continue;
                    LivingEntity trgt = c.getTarget();
                    if (c.getLocation().distanceSquared(trgt.getLocation()) <= 9.0) continue;
                    Projectile pj = null;
                    if (Mobs.getMobTier(c) == 1) {
                        pj = c.launchProjectile(Snowball.class);
                    }
                    if (Mobs.getMobTier(c) == 2) {
                        pj = c.launchProjectile(Snowball.class);
                    }
                    if (Mobs.getMobTier(c) == 3) {
                        pj = c.launchProjectile(Snowball.class);
                        pj.setVelocity(pj.getVelocity().multiply(1.25));
                    }
                    if (Mobs.getMobTier(c) == 4) {
                        pj = c.launchProjectile(Snowball.class);
                    }
                    if (Mobs.getMobTier(c) != 5) continue;
                    pj = c.launchProjectile(Snowball.class);
                    pj.setVelocity(pj.getVelocity().multiply(2));
                }
            }
        }.runTaskTimer(PracticeServer.plugin, 20, 25);
    }

    public void onDisable() {
        PracticeServer.log.info("[Mobs] has been disabled.");
    }

    static boolean isPlayerNearby(Creature c) {
        for (Entity ent : c.getNearbyEntities(12.0, 12.0, 12.0)) {
            if (ent == null || !(ent instanceof Player) || ent != c.getTarget()) continue;
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onHit(ProjectileHitEvent e) {
        Projectile pj = e.getEntity();
        if (pj.getShooter() != null && pj.getShooter() instanceof LivingEntity && !(pj.getShooter() instanceof Player)) {
            LivingEntity d = (LivingEntity) pj.getShooter();
            Player target = null;
            for (Entity ent : pj.getNearbyEntities(2.0, 1.5, 2.0)) {
                if (!(ent instanceof Player)) continue;
                target = (Player) ent;
            }
            if (target != null) {
                if (pj instanceof SmallFireball) {
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                }
                if (pj instanceof EnderPearl) {
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 2.0f, 1.5f);
                }
                target.damage(1.0, d);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntitySpawn(CreatureSpawnEvent e) {
        e.getEntity().getEquipment().clear();
    }

    @EventHandler
    public void onCubeSplit(SlimeSplitEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onCombust(EntityCombustEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player) && !e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            e.setCancelled(true);
            e.setDamage(0.0);
        }
    }

    public static int getMobTier(LivingEntity e) {
        if (e.getEquipment().getItemInMainHand() != null) {
            if (e.getEquipment().getItemInMainHand().getType().name().contains("WOOD_")) {
                return 1;
            }
            if (e.getEquipment().getItemInMainHand().getType().name().contains("STONE_")) {
                return 2;
            }
            if (e.getEquipment().getItemInMainHand().getType().name().contains("IRON_")) {
                return 3;
            }
            if (e.getEquipment().getItemInMainHand().getType().name().contains("DIAMOND_")) {
                return 4;
            }
            if (e.getEquipment().getItemInMainHand().getType().name().contains("GOLD_")) {
                return 5;
            }
        }
        return 0;
    }

    public static int getPlayerTier(Player e) {
        int tier = 0;
        ItemStack[] arritemStack = e.getInventory().getArmorContents();
        int n = arritemStack.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack is = arritemStack[n2];
            if (is != null && is.getType() != Material.AIR) {
                if (is.getType().name().contains("LEATHER_") && 1 > tier) {
                    tier = 1;
                }
                if (is.getType().name().contains("CHAINMAIL_") && 2 > tier) {
                    tier = 2;
                }
                if (is.getType().name().contains("IRON_") && 3 > tier) {
                    tier = 3;
                }
                if (is.getType().name().contains("DIAMOND_") && 4 > tier) {
                    tier = 4;
                }
                if (is.getType().name().contains("GOLD_") && 5 > tier) {
                    tier = 5;
                }
            }
            ++n2;
        }
        return tier;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKnockback(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity l = (LivingEntity) e.getEntity();
            if (e.getDamage() <= 0.0) {
                return;
            }
            if (!sound.containsKey(l.getUniqueId()) || sound.containsKey(l.getUniqueId()) && System.currentTimeMillis() - sound.get(l.getUniqueId()) > 500) {
                sound.put(l.getUniqueId(), System.currentTimeMillis());
                if (e.getEntity() instanceof Skeleton) {
                    if (e.getDamage() >= l.getHealth()) {
                        l.getWorld().playSound(l.getLocation(), Sound.ENTITY_SKELETON_DEATH, 1.0f, 1.0f);
                    }
                    l.getWorld().playSound(l.getLocation(), Sound.ENTITY_SKELETON_HURT, 1.0f, 1.0f);
                }
                if (e.getEntity() instanceof Zombie) {
                    if (e.getDamage() >= l.getHealth()) {
                        l.getWorld().playSound(l.getLocation(), Sound.ENTITY_ZOMBIE_DEATH, 1.0f, 1.0f);
                    }
                    l.getWorld().playSound(l.getLocation(), Sound.ENTITY_ZOMBIE_HURT, 1.0f, 1.0f);
                }
                if ((e.getEntity() instanceof Spider || e.getEntity() instanceof CaveSpider) && e.getDamage() >= l.getHealth()) {
                    l.getWorld().playSound(l.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
                }
                if (e.getEntity() instanceof PigZombie) {
                    if (e.getDamage() >= l.getHealth()) {
                        l.getWorld().playSound(l.getLocation(), Sound.ENTITY_ZOMBIE_PIG_DEATH, 1.0f, 1.0f);
                    }
                    l.getWorld().playSound(l.getLocation(), Sound.ENTITY_ZOMBIE_PIG_HURT, 1.0f, 1.0f);
                }
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (e.getReason() == EntityTargetEvent.TargetReason.CLOSEST_PLAYER && e.getTarget() instanceof Player && e.getEntity() instanceof Creature) {
            Creature l = (Creature) e.getEntity();
            Player p = (Player) e.getTarget();
            if (p.hasMetadata("NPC")) {
                e.setCancelled(true);
                e.setTarget(null);
                return;
            }
            if (Mobs.getPlayerTier(p) - Mobs.getMobTier(l) > 2) {
                e.setCancelled(true);
                e.setTarget(null);
                return;
            }
            if (l.hasPotionEffect(PotionEffectType.SLOW)) {
                l.removePotionEffect(PotionEffectType.SLOW);
                if (l.getEquipment().getItemInMainHand() != null && l.getEquipment().getItemInMainHand().getType().name().contains("_HOE")) {
                    l.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2), true);
                }
            }
            if (l.hasPotionEffect(PotionEffectType.JUMP)) {
                l.removePotionEffect(PotionEffectType.JUMP);
            }
        }
    }

    @EventHandler
    public void onEntityTargetLastHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Creature && e.getDamager() instanceof Player) {
            Creature c = (Creature) e.getEntity();
            Player p = (Player) e.getDamager();
            if (target.containsKey(c) && target.get(c) != null) {
                if (p.getLocation().distanceSquared(c.getLocation()) < target.get(c).getLocation().distanceSquared(c.getLocation())) {
                    c.setTarget(p);
                    target.put(c, p);
                }
            } else {
                c.setTarget(p);
                target.put(c, p);
            }
        }
    }

    public static boolean isElite(LivingEntity e) {
        return e.getEquipment().getItemInMainHand() != null && e.getEquipment().getItemInMainHand().getType() != Material.AIR && e.getEquipment().getItemInMainHand().getItemMeta().hasEnchants();
    }

    public static int getBarLength(int tier) {
        if (tier == 1) {
            return 25;
        }
        if (tier == 2) {
            return 30;
        }
        if (tier == 3) {
            return 35;
        }
        if (tier == 4) {
            return 40;
        }
        if (tier == 5) {
            return 50;
        }
        return 25;
    }

    public static String generateOverheadBar(Entity ent, double cur_hp, double max_hp, int tier, boolean elite) {
        int max_bar = Mobs.getBarLength(tier);
        ChatColor cc = null;
        DecimalFormatSymbols HpDot = new DecimalFormatSymbols(Locale.GERMAN);
        HpDot.setDecimalSeparator('.');
        HpDot.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("##.#", HpDot);
        double percent_hp = Math.round(100.0 * Double.parseDouble(df.format(cur_hp / max_hp)));
        if (percent_hp <= 0.0 && cur_hp > 0.0) {
            percent_hp = 1.0;
        }
        double percent_interval = 100.0 / (double) max_bar;
        int bar_count = 0;
        cc = ChatColor.GREEN;
        if (percent_hp <= 45.0) {
            cc = ChatColor.YELLOW;
        }
        if (percent_hp <= 20.0) {
            cc = ChatColor.RED;
        }
        if (crit.containsKey(ent) && cur_hp > 0.0) {
            cc = ChatColor.LIGHT_PURPLE;
        }
        String return_string = cc + ChatColor.BOLD.toString() + "\u2551" + ChatColor.RESET.toString() + cc.toString();
        if (elite) {
            return_string = String.valueOf(return_string) + ChatColor.BOLD.toString();
        }
        while (percent_hp > 0.0 && bar_count < max_bar) {
            percent_hp -= percent_interval;
            ++bar_count;
            return_string = String.valueOf(return_string) + "|";
        }
        return_string = String.valueOf(return_string) + ChatColor.BLACK.toString();
        if (elite) {
            return_string = String.valueOf(return_string) + ChatColor.BOLD.toString();
        }
        while (bar_count < max_bar) {
            return_string = String.valueOf(return_string) + "|";
            ++bar_count;
        }
        return_string = String.valueOf(return_string) + cc + ChatColor.BOLD.toString() + "\u2551";
        return return_string;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMobDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player)) {
            LivingEntity s = (LivingEntity) e.getEntity();
            if (e.getDamage() >= s.getHealth() && crit.containsKey(s)) {
                crit.remove(s);
                String mname = "";
                if (s.getEquipment().getItemInMainHand() != null && s.getEquipment().getItemInMainHand().getType() != Material.AIR) {
                    mname = Mobs.generateOverheadBar(s, 0.0, s.getMaxHealth(), Mobs.getMobTier(s), Mobs.isElite(s));
                    s.setCustomName(mname);
                    s.setCustomNameVisible(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCrit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player) && e.getDamager() instanceof Player) {
            if (e.getDamage() <= 0.0) {
                return;
            }
            LivingEntity s = (LivingEntity) e.getEntity();
            Random random = new Random();
            int rcrt = random.nextInt(100) + 1;
            if (!crit.containsKey(s) && (Mobs.getMobTier(s) == 1 && rcrt <= 5 || Mobs.getMobTier(s) == 2 && rcrt <= 7 || Mobs.getMobTier(s) == 3 && rcrt <= 10 || Mobs.getMobTier(s) == 4 && rcrt <= 13 || Mobs.getMobTier(s) == 5 && rcrt <= 20)) {
                crit.put(s, 4);
                if (Mobs.isElite(s)) {
                    s.getWorld().playSound(s.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1.0f, 4.0f);
                    double max = s.getMaxHealth();
                    double hp = s.getHealth() - e.getDamage();
                    s.setCustomName(Mobs.generateOverheadBar(s, hp, max, Mobs.getMobTier(s), Mobs.isElite(s)));
                    s.setCustomNameVisible(true);
                    Listeners.named.put(s.getUniqueId(), System.currentTimeMillis());
                    s.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10), true);
                    s.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 127), true);
                } else {
                    s.getWorld().playSound(s.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1.0f, 2.0f);
                    double max = s.getMaxHealth();
                    double hp = s.getHealth() - e.getDamage();
                    s.setCustomName(Mobs.generateOverheadBar(s, hp, max, Mobs.getMobTier(s), Mobs.isElite(s)));
                    s.setCustomNameVisible(true);
                    Listeners.named.put(s.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler
    public void onSafeSpot(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof LivingEntity && !(e.getEntity() instanceof Player) && e.getDamager() instanceof Player) {
            if (e.getDamage() <= 0.0) {
                return;
            }
            LivingEntity s = (LivingEntity) e.getEntity();
            Player p = (Player) e.getDamager();
            Random random = new Random();
            int rcrt = random.nextInt(10) + 1;
            if (rcrt == 1 && p.getLocation().getY() - s.getLocation().getY() > 1.0 && p.getLocation().distance(s.getLocation()) <= 6.0) {
                s.teleport(p.getLocation().add(0.0, 0.25, 0.0));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMobHitMob(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof LivingEntity && !(e.getDamager() instanceof Player) && !(e.getEntity() instanceof Player)) {
            e.setCancelled(true);
            e.setDamage(0.0);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onMobHit(EntityDamageByEntityEvent e) {
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getDamager() instanceof LivingEntity && !(e.getDamager() instanceof Player) && e.getEntity() instanceof Player) {
            LivingEntity s = (LivingEntity) e.getDamager();
            Player p = (Player) e.getEntity();
            Random random = new Random();
            int dmg = 1;
            if (s.getEquipment().getItemInMainHand() != null && s.getEquipment().getItemInMainHand().getType() != Material.AIR) {
                int min = Damage.getDamageRange(s.getEquipment().getItemInMainHand()).get(0);
                int max = Damage.getDamageRange(s.getEquipment().getItemInMainHand()).get(1);
                dmg = random.nextInt(max - min + 1) + min + 1;
            }
            if (crit.containsKey(s) && crit.get(s) == 0) {
                dmg = Mobs.isElite(s) ? (dmg *= 4) : (dmg *= 3);
                if (!Mobs.isElite(s)) {
                    crit.remove(s);
                }
                p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.3f);
                double max = s.getMaxHealth();
                double hp = s.getHealth() - e.getDamage();
                s.setCustomName(Mobs.generateOverheadBar(s, hp, max, Mobs.getMobTier(s), Mobs.isElite(s)));
                s.setCustomNameVisible(true);
                Listeners.named.put(s.getUniqueId(), System.currentTimeMillis());
            }
            if (e.getDamage() <= 0.0) {
                return;
            }
            if (s.getEquipment().getItemInMainHand().getType().name().contains("WOOD_")) {
                dmg = s.getEquipment().getItemInMainHand().getItemMeta().hasEnchants() ? (int) ((double) dmg * 2.5) : (int) ((double) dmg * 0.8);
            }
            if (s.getEquipment().getItemInMainHand().getType().name().contains("STONE_")) {
                dmg = s.getEquipment().getItemInMainHand().getItemMeta().hasEnchants() ? (int) ((double) dmg * 2.5) : (int) ((double) dmg * 0.9);
            }
            if (s.getEquipment().getItemInMainHand().getType().name().contains("IRON_")) {
                dmg = s.getEquipment().getItemInMainHand().getItemMeta().hasEnchants() ? (dmg *= 3) : (int) ((double) dmg * 1.2);
            }
            if (s.getEquipment().getItemInMainHand().getType().name().contains("DIAMOND_")) {
                dmg = s.getEquipment().getItemInMainHand().getItemMeta().hasEnchants() ? (dmg *= 5) : (int) ((double) dmg * 1.4);
            }
            if (s.getEquipment().getItemInMainHand().getType().name().contains("GOLD_")) {
                dmg = s.getEquipment().getItemInMainHand().getItemMeta().hasEnchants() ? (dmg *= 7) : (dmg *= 2);
            }
            if (s instanceof MagmaCube) {
                dmg = (int) ((double) dmg * 0.5);
            }
            if (dmg < 1) {
                dmg = 1;
            }
            e.setDamage((double) dmg);
        }
    }

}

