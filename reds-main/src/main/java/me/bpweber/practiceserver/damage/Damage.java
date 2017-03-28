/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  me.confuser.barapi.BarAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.EntityEffect
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.Vector
 */
package me.bpweber.practiceserver.damage;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.line.*;
import me.bpweber.practiceserver.*;
import me.bpweber.practiceserver.player.*;
import me.bpweber.practiceserver.pvp.*;
import me.bpweber.practiceserver.utils.*;
import me.bpweber.practiceserver.vendors.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.scheduler.*;
import org.bukkit.util.Vector;

import java.util.*;

public class Damage
        implements Listener {
    HashMap<Player, Long> playerslow = new HashMap<Player, Long>();
    public static HashMap<Player, Long> lasthit = new HashMap<Player, Long>();
    public static HashMap<Player, Player> lastphit = new HashMap<Player, Player>();
    HashMap<UUID, Long> kb = new HashMap<UUID, Long>();
    ArrayList<String> p_arm = new ArrayList<String>();

    public void onEnable() {
        PracticeServer.log.info("[Damage] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {

                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    double healthPercentage = (p.getHealth() / p.getMaxHealth());
                    if (healthPercentage * 100.0F > 100.0F) {
                        healthPercentage = 1.0;
                    }
                    float pcnt = (float) (healthPercentage * 1.F);
                    if (!Alignments.playerBossBars.containsKey(p)) {
                        // Set new one
                        BossBar bossBar = Bukkit.createBossBar(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "HP " + ChatColor.LIGHT_PURPLE
                                + (int) p.getHealth() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " / "
                                + ChatColor.LIGHT_PURPLE + (int) p.getMaxHealth(), BarColor.PINK, BarStyle.SOLID);
                        bossBar.addPlayer(p);
                        Alignments.playerBossBars.put(p, bossBar);
                        Alignments.playerBossBars.get(p).setProgress(pcnt);
                    } else {
                        Alignments.playerBossBars.get(p).setTitle(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "HP " + ChatColor.LIGHT_PURPLE
                                + (int) p.getHealth() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " / "
                                + ChatColor.LIGHT_PURPLE + (int) p.getMaxHealth());
                        Alignments.playerBossBars.get(p).setProgress(pcnt);
                    }
                }
            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 0, 1);
        new BukkitRunnable() {

            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (Damage.this.playerslow.containsKey(p)) {
                        if (System.currentTimeMillis() - Damage.this.playerslow.get(p) <= 3000) continue;
                        syncSpeed(p, 0.2f);
                        continue;
                    }
                    if (p.getWalkSpeed() == 0.2f) continue;
                    syncSpeed(p, 0.2f);
                }
            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 20, 20);
    }

    public void syncSpeed(Player player, float f) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.plugin, () -> player.setWalkSpeed(f));
    }

    public void onDisable() {
        PracticeServer.log.info("[Damage] has been disabled.");
    }

    public static int getHp(ItemStack is) {
        List<String> lore;
        if (is != null && is.getType() != Material.AIR && is.getItemMeta().hasLore() && (lore = is.getItemMeta().getLore()).size() > 1 && lore.get(1).contains("HP")) {
            try {
                return Integer.parseInt(lore.get(1).split(": +")[1]);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static int getArmor(ItemStack is) {
        List<String> lore;
        if (is != null && is.getType() != Material.AIR && is.getItemMeta().hasLore() && (lore = is.getItemMeta().getLore()).size() > 0 && lore.get(0).contains("ARMOR")) {
            try {
                return Integer.parseInt(lore.get(0).split(" - ")[1].split("%")[0]);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static int getDps(ItemStack is) {
        List<String> lore;
        if (is != null && is.getType() != Material.AIR && is.getItemMeta().hasLore() && (lore = is.getItemMeta().getLore()).size() > 0 && lore.get(0).contains("DPS")) {
            try {
                return Integer.parseInt(lore.get(0).split(" - ")[1].split("%")[0]);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static int getEnergy(ItemStack is) {
        List<String> lore;
        if (is != null && is.getType() != Material.AIR && is.getItemMeta().hasLore() && (lore = is.getItemMeta().getLore()).size() > 2 && lore.get(2).contains("ENERGY REGEN")) {
            try {
                return Integer.parseInt(lore.get(2).split(": +")[1].split("%")[0]);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static int getHps(ItemStack is) {
        List<String> lore;
        if (is != null && is.getType() != Material.AIR && is.getItemMeta().hasLore() && (lore = is.getItemMeta().getLore()).size() > 2 && lore.get(2).contains("HP REGEN")) {
            try {
                return Integer.parseInt(lore.get(2).split(": +")[1].split(" HP/s")[0]);
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static int getPercent(ItemStack is, String type) {
        if (is != null && is.getType() != Material.AIR && is.getItemMeta().hasLore()) {
            List<String> lore = is.getItemMeta().getLore();
            for (String s : lore) {
                if (!s.contains(type)) continue;
                try {
                    return Integer.parseInt(s.split(": ")[1].split("%")[0]);
                } catch (Exception e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public static int getElem(ItemStack is, String type) {
        if (is != null && is.getType() != Material.AIR && is.getItemMeta().hasLore()) {
            List<String> lore = is.getItemMeta().getLore();
            for (String s : lore) {
                if (!s.contains(type)) continue;
                try {
                    return Integer.parseInt(s.split(": +")[1]);
                } catch (Exception e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    public static List<Integer> getDamageRange(ItemStack is) {
        List<String> lore;
        ArrayList<Integer> dmg = new ArrayList<Integer>();
        dmg.add(1);
        dmg.add(1);
        if (is != null && is.getType() != Material.AIR && is.getItemMeta().hasLore() && (lore = is.getItemMeta().getLore()).size() > 0 && lore.get(0).contains("DMG")) {
            try {
                int min = 1;
                int max = 1;
                min = Integer.parseInt(lore.get(0).split("DMG: ")[1].split(" - ")[0]);
                max = Integer.parseInt(lore.get(0).split(" - ")[1]);
                dmg.set(0, min);
                dmg.set(1, max);
            } catch (Exception e) {
                dmg.set(0, 1);
                dmg.set(1, 1);
            }
        }
        return dmg;
    }

    public static int getCrit(Player p) {
        int crit = 0;
        ItemStack wep = p.getInventory().getItemInMainHand();
        if (Staffs.staff.containsKey(p)) {
            wep = Staffs.staff.get(p);
        }
        if (wep != null && wep.getType() != Material.AIR && wep.getItemMeta().hasLore()) {
            List<String> lore = wep.getItemMeta().getLore();
            for (String line : lore) {
                if (!line.contains("CRITICAL HIT")) continue;
                crit = Damage.getPercent(wep, "CRITICAL HIT");
            }
            if (wep.getType().name().contains("_AXE")) {
                crit += 3;
            }
            int intel = 0;
            ItemStack[] arritemStack = p.getInventory().getArmorContents();
            int n = arritemStack.length;
            int n2 = 0;
            while (n2 < n) {
                ItemStack is = arritemStack[n2];
                if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                    int addint = Damage.getElem(is, "INT");
                    intel += addint;
                }
                ++n2;
            }
            if (intel > 0) {
                crit = (int) ((long) crit + Math.round((double) intel * 0.011));
            }
        }
        return crit;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNpcDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.hasMetadata("NPC") || p.getPlayerListName().equals("")) {
                e.setCancelled(true);
                e.setDamage(0.0);
            }
            if (p.isOp() || p.getGameMode() == GameMode.CREATIVE || p.isFlying()) {
                e.setCancelled(true);
                e.setDamage(0.0);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNoOpAndGuildDamage(EntityDamageByEntityEvent e) {
        Player p;
        GPlayer nPlayer = PlayerManager.getPlayer(((Player) e.getEntity()).getPlayer());
        GPlayer nPlayerAttacker = PlayerManager.getPlayer(((Player) e.getDamager()).getPlayer());
        if(nPlayer.hasGuild() && nPlayerAttacker.hasGuild())
        {
            if(nPlayer.getGuild().isMember(nPlayerAttacker) && nPlayer.getGuild().getFriendlyPvp())
            {
                e.setCancelled(true);
                e.setDamage(0.0);
                ((LivingEntity) e.getEntity()).setNoDamageTicks(0);
            }
            if(nPlayer.getGuild().isAlly(nPlayerAttacker.getGuild()))
            {
                e.setCancelled(true);
                e.setDamage(0.0);
                ((LivingEntity) e.getEntity()).setNoDamageTicks(0);
            }
        }
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity && ((p = (Player) e.getDamager()).isOp() || p.getGameMode() == GameMode.CREATIVE || p.isFlying())) {
            e.setCancelled(true);
            e.setDamage(0.0);
            ((LivingEntity) e.getEntity()).setNoDamageTicks(0);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onThorns(EntityDamageByEntityEvent e) {
        Player p;
        LivingEntity le;
        Random random;
        int thornsamt = 0;

        if (e.getEntity() instanceof Player) {
            p = (Player) e.getEntity();
            ItemStack[] arritemStack = p.getInventory().getArmorContents();
            int n = arritemStack.length;
            int n2 = 0;
            while (n2 < n) {
                ItemStack is = arritemStack[n2];
                if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                    thornsamt = thornsamt + Damage.getPercent(is, "THORNS");
                }
                ++n2;
            }
            if (e.getDamager() instanceof LivingEntity) {
                le = (LivingEntity) e.getDamager();
                if (thornsamt > 0) {
                    if (!e.isCancelled()) {
                        if (e.getDamage() > 0.0) {
                            p.getWorld().playEffect(p.getEyeLocation(), Effect.STEP_SOUND, Material.LEAVES);
                            int dmg = (int) e.getDamage();
                            int afterDmg = dmg * thornsamt / 200;
                            le.damage(afterDmg);
                        }
                    }
                }
            }
        }
    }

    public void callHGDMG(Player p, LivingEntity le, String d, int dmg) {
        ArrayList<String> gettoggles = Toggles.getToggles(p.getName());

        if (gettoggles.contains("holodmg")) {
            Random r = new Random();
            float x = r.nextFloat();
            float y = r.nextFloat();
            float z = r.nextFloat();
            int dmgs = dmg;
            Hologram hg = new Hologram("dmg", le.getLocation().add(x, 0.5 + y, z));
            if (d.equalsIgnoreCase("dmg")) {
                HologramLine line = new TextLine(hg, ChatColor.RED + "-" + dmg + "❤");
                hg.addLine(line);
                hg.spawn();
            }
            if (d.equalsIgnoreCase("dodge")) {
                HologramLine line = new TextLine(hg, ChatColor.RED + "*DODGE*");
                hg.addLine(line);
                hg.spawn();
            }
            if (d.equalsIgnoreCase("block")) {
                HologramLine line = new TextLine(hg, ChatColor.RED + "*BLOCK*");
                hg.addLine(line);
                hg.spawn();
            }
            PracticeServer.plugin.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.plugin, new Runnable() {
                @Override
                public void run() {
                    hg.teleport(hg.getLocation().add(0, 0.15, 0));
                }
            }, 5L);
            PracticeServer.plugin.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.plugin, new Runnable() {
                @Override
                public void run() {
                    hg.teleport(hg.getLocation().add(0, 0.15, 0));
                }
            }, 8L);
            PracticeServer.plugin.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.plugin, new Runnable() {
                @Override
                public void run() {
                    hg.teleport(hg.getLocation().add(0, 0.15, 0));
                }
            }, 10L);
            PracticeServer.plugin.getServer().getScheduler().scheduleSyncDelayedTask(PracticeServer.plugin, new Runnable() {
                @Override
                public void run() {
                    hg.teleport(hg.getLocation().add(0, 0.15, 0));
                }
            }, 15L);

            new BukkitRunnable() {
                @Override
                public void run() {
                    hg.teleport(hg.getLocation().add(0, 0.15, 0));
                    hg.despawn();
                }
            }.runTaskLaterAsynchronously(PracticeServer.plugin, 20L);
        }
    }

    @EventHandler
    public void holoDMG(EntityDamageByEntityEvent e) {
        ArrayList<String> gettoggles = Toggles.getToggles(e.getDamager().getName());
        if (e.getEntity() instanceof LivingEntity && e.getDamager() instanceof Player) {
            if (e.getDamage() > 0 && !e.isCancelled()) {
                Player p = (Player) e.getDamager();
                LivingEntity le = (LivingEntity) e.getEntity();
                int dmg = (int) e.getDamage();
                callHGDMG(p, le, "dmg", dmg);
            }
        }
    }


    @EventHandler(priority = EventPriority.LOW)
    public void onBlodge(EntityDamageByEntityEvent e) {
        Player p;
        Random random;
        if (e.getEntity() instanceof Player) {
            if (e.getDamage() <= 0.0) {
                return;
            }
            p = (Player) e.getEntity();
            PlayerInventory i = p.getInventory();
            p.setNoDamageTicks(0);
            int block = 0;
            int dodge = 0;
            if (p.getHealth() > 0.0) {
                ItemStack[] arritemStack = i.getArmorContents();
                int n = arritemStack.length;
                int n2 = 0;
                while (n2 < n) {
                    ItemStack is = arritemStack[n2];
                    if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                        int addedblock = Damage.getPercent(is, "BLOCK");
                        block += addedblock;
                        int addeddodge = Damage.getPercent(is, "DODGE");
                        dodge += addeddodge;
                    }
                    ++n2;
                }
                int str = 0;
                ItemStack[] addedblock = p.getInventory().getArmorContents();
                int n3 = addedblock.length;
                n = 0;
                while (n < n3) {
                    ItemStack is = addedblock[n];
                    if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                        int addstr = Damage.getElem(is, "STR");
                        str += addstr;
                    }
                    ++n;
                }
                if (str > 0) {
                    block = (int) ((long) block + Math.round((double) str * 0.015));
                }
                random = new Random();
                int dodger = random.nextInt(100) + 1;
                int blockr = random.nextInt(100) + 1;
                if (e.getDamager() instanceof Player) {
                    int accuracy;
                    Player d = (Player) e.getDamager();
                    ItemStack wep = d.getInventory().getItemInMainHand();
                    if (Staffs.staff.containsKey(d)) {
                        wep = Staffs.staff.get(d);
                    }
                    if ((accuracy = Damage.getPercent(wep, "ACCURACY")) > 0) {
                        int b4block = block;
                        int b4dodge = dodge;
                        if (accuracy > 0) {
                            block -= accuracy;
                            accuracy -= b4block;
                        }
                        if (accuracy > 0) {
                            dodge -= accuracy;
                            accuracy -= b4dodge;
                        }
                    }
                    if (blockr <= block) {
                        e.setDamage(0.0);
                        e.setCancelled(true);
                        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.0f, 1.0f);
                        callHGDMG(d, p, "block", 0);
                        d.sendMessage("          " + ChatColor.RED + ChatColor.BOLD + "*OPPONENT BLOCKED* (" + p.getName() + ")");
                        p.sendMessage("          " + ChatColor.DARK_GREEN + ChatColor.BOLD + "*BLOCK* (" + d.getName() + ")");
                    } else if (dodger <= dodge) {
                        e.setDamage(0.0);
                        e.setCancelled(true);
                        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1.0f, 1.0f);
                        callHGDMG(d, p, "dodge", 0);
                        d.sendMessage("          " + ChatColor.RED + ChatColor.BOLD + "*OPPONENT DODGED* (" + p.getName() + ")");
                        p.sendMessage("          " + ChatColor.GREEN + ChatColor.BOLD + "*DODGE* (" + d.getName() + ")");
                    } else if (blockr <= 80 && p.isBlocking()) {
                        e.setDamage((double) ((int) e.getDamage() / 2));
                        callHGDMG(d, p, "block", 0);
                        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.0f, 1.0f);
                        d.sendMessage("          " + ChatColor.RED + ChatColor.BOLD + "*OPPONENT BLOCKED* (" + p.getName() + ")");
                        p.sendMessage("          " + ChatColor.DARK_GREEN + ChatColor.BOLD + "*BLOCK* (" + d.getName() + ")");
                    }
                } else if (e.getDamager() instanceof LivingEntity) {
                    LivingEntity li = (LivingEntity) e.getDamager();
                    String mname = "";
                    if (li.hasMetadata("name")) {
                        mname = li.getMetadata("name").get(0).asString();
                    }
                    if (blockr <= block) {
                        e.setDamage(0.0);
                        e.setCancelled(true);
                        callHGDMG(p, li, "block", 0);
                        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.0f, 1.0f);
                        p.sendMessage("          " + ChatColor.DARK_GREEN + ChatColor.BOLD + "*BLOCK* (" + mname + ChatColor.DARK_GREEN + ")");
                    } else if (dodger <= dodge) {
                        e.setDamage(0.0);
                        e.setCancelled(true);
                        callHGDMG(p, li, "dodge", 0);
                        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1.0f, 1.0f);
                        p.sendMessage("          " + ChatColor.GREEN + ChatColor.BOLD + "*DODGE* (" + mname + ChatColor.GREEN + ")");
                    } else if (blockr <= 80 && p.isBlocking()) {
                        e.setDamage((double) ((int) e.getDamage() / 2));
                        callHGDMG(p, li, "block", 0);
                        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1.0f, 1.0f);
                        p.sendMessage("          " + ChatColor.DARK_GREEN + ChatColor.BOLD + "*BLOCK* (" + mname + ChatColor.DARK_GREEN + ")");
                    }
                }
            }
        }
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
            p = (Player) e.getDamager();
            LivingEntity li = (LivingEntity) e.getEntity();
            ItemStack wep = p.getInventory().getItemInMainHand();
            if (Staffs.staff.containsKey(p)) {
                wep = Staffs.staff.get(p);
            }
            if (wep != null && wep.getType() != Material.AIR && wep.getItemMeta().hasLore()) {
                int min = Damage.getDamageRange(wep).get(0);
                int max = Damage.getDamageRange(wep).get(1);
                p.setNoDamageTicks(0);
                random = new Random();
                int dmg = random.nextInt(max - min + 1) + min;
                int tier = Merchant.getTier(wep);
                List<String> lore = wep.getItemMeta().getLore();
                for (String line : lore) {
                    int eldmg;
                    if (line.contains("ICE DMG")) {
                        li.getWorld().playEffect(li.getLocation().add(0.0, 1.3, 0.0), Effect.POTION_BREAK, 8194);
                        eldmg = Damage.getElem(wep, "ICE DMG");
                        dmg += eldmg;
                        if (tier == 1) {
                            li.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));
                        }
                        if (tier == 2) {
                            li.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 0));
                        }
                        if (tier == 3) {
                            li.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1));
                        }
                        if (tier == 4) {
                            li.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 50, 1));
                        }
                        if (tier == 5) {
                            li.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                        }
                    }
                    if (line.contains("POISON DMG")) {
                        li.getWorld().playEffect(li.getLocation().add(0.0, 1.3, 0.0), Effect.POTION_BREAK, 8196);
                        eldmg = Damage.getElem(wep, "POISON DMG");
                        dmg += eldmg;
                        if (tier == 1) {
                            li.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 15, 0));
                        }
                        if (tier == 2) {
                            li.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 25, 0));
                        }
                        if (tier == 3) {
                            li.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 30, 1));
                        }
                        if (tier == 4) {
                            li.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 35, 1));
                        }
                        if (tier == 5) {
                            li.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 1));
                        }
                    }
                    if (line.contains("FIRE DMG")) {
                        eldmg = Damage.getElem(wep, "FIRE DMG");
                        dmg += eldmg;
                        if (tier == 1) {
                            li.setFireTicks(15);
                        }
                        if (tier == 2) {
                            li.setFireTicks(25);
                        }
                        if (tier == 3) {
                            li.setFireTicks(30);
                        }
                        if (tier == 4) {
                            li.setFireTicks(35);
                        }
                        if (tier == 5) {
                            li.setFireTicks(40);
                        }
                    }
                    if (!line.contains("PURE DMG")) continue;
                    eldmg = Damage.getElem(wep, "PURE DMG");
                    dmg += eldmg;
                }
                int crit = Damage.getCrit(p);
                int drop = random.nextInt(100) + 1;
                if (drop <= crit) {
                    dmg *= 2;
                    p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.5f, 0.5f);
                    Particles.CRIT_MAGIC.display(0.0f, 0.0f, 0.0f, 1.0f, 50, li.getLocation(), 20.0);
                }
                PlayerInventory i = p.getInventory();
                double dps = 0.0;
                double vit = 0.0;
                double str = 0.0;
                ItemStack[] arritemStack = i.getArmorContents();
                int n = arritemStack.length;
                int n4 = 0;
                while (n4 < n) {
                    ItemStack is = arritemStack[n4];
                    if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                        int adddps = Damage.getDps(is);
                        dps += (double) adddps;
                        int addvit = Damage.getElem(is, "VIT");
                        vit += (double) addvit;
                        int addstr = Damage.getElem(is, "STR");
                        str += (double) addstr;
                    }
                    ++n4;
                }
                if (vit > 0.0 && wep.getType().name().contains("_SWORD")) {
                    double divide = vit / 5000.0;
                    double pre = (double) dmg * divide;
                    dmg = (int) ((double) dmg + pre);
                }
                if (str > 0.0 && wep.getType().name().contains("_AXE")) {
                    double divide = str / 5000.0;
                    double pre = (double) dmg * divide;
                    dmg = (int) ((double) dmg + pre);
                }
                if (dps > 0.0) {
                    double divide = dps / 100.0;
                    double pre = (double) dmg * divide;
                    dmg = (int) ((double) dmg + pre);
                }
                for (String line2 : lore) {
                    ArrayList<String> toggles;
                    if (!line2.contains("LIFE STEAL")) continue;
                    li.getWorld().playEffect(li.getEyeLocation(), Effect.STEP_SOUND, Material.REDSTONE_WIRE);
                    double base = Damage.getPercent(wep, "LIFE STEAL");
                    double pcnt = base / 100.0;
                    int life = 1;
                    if ((int) (pcnt * (double) dmg) > 0) {
                        life = (int) (pcnt * (double) dmg);
                    }
                    if (p.getHealth() < p.getMaxHealth() - (double) life) {
                        p.setHealth(p.getHealth() + (double) life);
                        toggles = Toggles.getToggles(p.getName());
                        if (!toggles.contains("debug")) continue;
                        p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "            +" + ChatColor.GREEN + life + ChatColor.GREEN + ChatColor.BOLD + " HP " + ChatColor.GRAY + "[" + (int) p.getHealth() + "/" + (int) p.getMaxHealth() + "HP]");
                        continue;
                    }
                    if (p.getHealth() < p.getMaxHealth() - (double) life) continue;
                    p.setHealth(p.getMaxHealth());
                    toggles = Toggles.getToggles(p.getName());
                    if (!toggles.contains("debug")) continue;
                    p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "            +" + ChatColor.GREEN + life + ChatColor.GREEN + ChatColor.BOLD + " HP " + ChatColor.GRAY + "[" + (int) p.getMaxHealth() + "/" + (int) p.getMaxHealth() + "HP]");
                }
                e.setDamage((double) dmg);
                return;
            }
            e.setDamage(1.0);
        }
    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onArmor(EntityDamageEvent e) {
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            PlayerInventory i = p.getInventory();
            double dmg = e.getDamage();
            double arm = 0.0;
            ItemStack[] arritemStack = i.getArmorContents();
            int n = arritemStack.length;
            int n2 = 0;
            while (n2 < n) {
                ItemStack is = arritemStack[n2];
                if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                    int addarm = Damage.getArmor(is);
                    arm += (double) addarm;
                }
                ++n2;
            }
            if (arm > 0.0) {
                double divide = arm / 100.0;
                double pre = dmg * divide;
                int cleaned = (int) (dmg - pre);
                if (cleaned <= 1) {
                    cleaned = 1;
                }
                dmg = cleaned;
                int health = 0;
                if (p.getHealth() - (double) cleaned > 0.0) {
                    health = (int) (p.getHealth() - (double) cleaned);
                }
                if ((Toggles.getToggles(p.getName())).contains("debug")) {
                    if (health < 0) {
                        health = 0;
                    }
                    p.sendMessage(ChatColor.RED + "            -" + cleaned + ChatColor.RED + ChatColor.BOLD + "HP " + ChatColor.GRAY + "[-" + (int) arm + "%A -> -" + (int) pre + ChatColor.BOLD + "DMG" + ChatColor.GRAY + "] " + ChatColor.GREEN + "[" + health + ChatColor.BOLD + "HP" + ChatColor.GREEN + "]");
                }
                e.setDamage((double) cleaned);
            } else {
                ArrayList<String> toggles = Toggles.getToggles(p.getName());
                if (toggles.contains("debug")) {
                    int health = (int) (p.getHealth() - dmg);
                    if (health < 0) {
                        health = 0;
                    }
                    p.sendMessage(ChatColor.RED + "            -" + (int) dmg + ChatColor.RED + ChatColor.BOLD + "HP " + ChatColor.GRAY + "[-0%A -> -0" + ChatColor.BOLD + "DMG" + ChatColor.GRAY + "] " + ChatColor.GREEN + "[" + health + ChatColor.BOLD + "HP" + ChatColor.GREEN + "]");
                }

                e.setDamage(dmg);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDebug(EntityDamageByEntityEvent e) {
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player d = (Player) e.getDamager();
            int dmg = (int) e.getDamage();
            p.setNoDamageTicks(0);
            ArrayList<String> toggles = Toggles.getToggles(d.getName());
            if (toggles.contains("debug")) {
                d.sendMessage(ChatColor.RED + "            " + dmg + ChatColor.RED + ChatColor.BOLD + " DMG " + ChatColor.RED + "-> " + p.getName());
            }
            lastphit.put(p, d);
            lasthit.put(p, System.currentTimeMillis());
        } else if (e.getEntity() instanceof LivingEntity && e.getDamager() instanceof Player) {
            LivingEntity p = (LivingEntity) e.getEntity();
            Player d = (Player) e.getDamager();
            p.setNoDamageTicks(0);
            int dmg = (int) e.getDamage();
            int health = 0;
            if (p.getHealth() - (double) dmg > 0.0) {
                health = (int) (p.getHealth() - (double) dmg);
            }
            String name = "";
            if (p.hasMetadata("name")) {
                name = p.getMetadata("name").get(0).asString();
            }
            if ((Toggles.getToggles(d.getName())).contains("debug")) {
                d.sendMessage(ChatColor.RED + "            " + dmg + ChatColor.RED + ChatColor.BOLD + " DMG " + ChatColor.RED + "-> " + ChatColor.RESET + name + " [" + health + "HP]");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKnockback(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof LivingEntity && e.getDamager() instanceof LivingEntity) {

            LivingEntity p = (LivingEntity) e.getEntity();
            LivingEntity d = (LivingEntity) e.getDamager();
            p.setNoDamageTicks(0);
            if (e.getDamage() <= 0.0) {
                return;
            }
            if (p instanceof Player) {
                Vector v = p.getLocation().toVector().subtract(d.getLocation().toVector());
                if (v.getX() != 0.0 || v.getY() != 0.0 || v.getZ() != 0.0) {
                    v.normalize();
                }
                p.setVelocity(v.multiply(0.24f));
            } else if (!this.kb.containsKey(p.getUniqueId()) || this.kb.containsKey(p.getUniqueId()) && System.currentTimeMillis() - this.kb.get(p.getUniqueId()) > 500) {
                this.kb.put(p.getUniqueId(), System.currentTimeMillis());
                Vector v = p.getLocation().toVector().subtract(d.getLocation().toVector());
                if (v.getX() != 0.0 || v.getY() != 0.0 || v.getZ() != 0.0) {
                    v.normalize();
                }
                if (d instanceof Player) {
                    Player dam = (Player) d;
                    if (dam.getInventory().getItemInMainHand() != null && dam.getInventory().getItemInMainHand().getType().name().contains("_SPADE")) {
                        p.setVelocity(v.multiply(0.7F).setY(0.));
                    } else {
                        p.setVelocity(v.multiply(0.5f).setY(0.35));
                    }
                } else {
                    p.setVelocity(v.multiply(0.5f).setY(0.35));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity s = (LivingEntity) e.getEntity();
            if (e.getDamage() >= s.getHealth() && this.kb.containsKey(s.getUniqueId())) {
                this.kb.remove(s.getUniqueId());
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPolearmAOE(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof LivingEntity && e.getDamager() instanceof Player) {
            LivingEntity le = (LivingEntity) e.getEntity();
            Player p = (Player) e.getDamager();
            if (e.getDamage() <= 0.0) {
                return;
            }
            if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType().name().contains("_SPADE") && !this.p_arm.contains(p.getName())) {
                int amt = 5;
                Energy.removeEnergy(p, amt);
                for (Entity near : le.getNearbyEntities(1, 2, 1)) {
                    if (!(near instanceof LivingEntity) || near == le || near == p || near == null) continue;
                    LivingEntity n = (LivingEntity) near;
                    le.setNoDamageTicks(0);
                    n.setNoDamageTicks(0);
                    if (Energy.nodamage.containsKey(p.getName())) {
                        Energy.nodamage.remove(p.getName());
                    }
                    this.p_arm.add(p.getName());
                    n.damage(1.0, p);
                    this.p_arm.remove(p.getName());
                }
            }
        }
    }

    @EventHandler
    public void onDamageSound(EntityDamageByEntityEvent e) {
        Player p;
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
            if (e.getDamage() <= 0.0) {
                return;
            }
            p = (Player) e.getDamager();
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
            if (e.getEntity() instanceof Player) {
                e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 1.6f);
            }
        }
        if (e.getEntity() instanceof Player && !(e.getDamager() instanceof Player) && e.getDamager() instanceof LivingEntity) {
            p = (Player) e.getEntity();
            p.setWalkSpeed(0.165f);
            this.playerslow.put(p, System.currentTimeMillis());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBypassArmor(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity li = (LivingEntity) e.getEntity();
            if (e.getDamage() <= 0.0) {
                return;
            }
            int dmg = (int) e.getDamage();
            e.setDamage(0.0);
            e.setCancelled(true);
            li.playEffect(EntityEffect.HURT);
            li.setLastDamageCause(e);
            if (li.getHealth() - (double) dmg <= 0.0) {
                li.setHealth(0.0);
            } else {
                li.setHealth(li.getHealth() - (double) dmg);
            }
        }
    }
}

