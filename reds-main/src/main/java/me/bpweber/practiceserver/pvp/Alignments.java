package me.bpweber.practiceserver.pvp;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.google.common.collect.Maps;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Setrank;
import me.bpweber.practiceserver.damage.Damage;
import me.bpweber.practiceserver.damage.Staffs;
import me.bpweber.practiceserver.party.Scoreboards;
import me.bpweber.practiceserver.player.Listeners;
import me.bpweber.practiceserver.teleport.TeleportBooks;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBarAPI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Alignments
        implements Listener {
    public static HashMap<String, Integer> neutral = new HashMap<String, Integer>();
    public static HashMap<String, Integer> chaotic = new HashMap<String, Integer>();
    public static HashMap<String, Long> tagged = new HashMap<String, Long>();

    public static HashMap<Player, BossBar> playerBossBars;

    public void onEnable() {
        this.playerBossBars = Maps.newHashMap();
        int time;
        PracticeServer.log.info("[Alignments] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (p.isOnline()) {
                        int time;
                        if (Alignments.chaotic.containsKey(p.getName())) {
                            time = Alignments.chaotic.get(p.getName());
                            if (time <= 1) {
                                Alignments.chaotic.remove(p.getName());
                                Alignments.neutral.put(p.getName(), 120);
                                Alignments.updatePlayerAlignment(p);
                                p.sendMessage(ChatColor.YELLOW + "          * YOU ARE NOW " + ChatColor.BOLD + "NEUTRAL" + ChatColor.YELLOW + " ALIGNMENT *");
                                p.sendMessage(ChatColor.GRAY + "While neutral, players who kill you will not become chaotic. You have a 50% chance of dropping your weapon, and a 25% chance of dropping each piece of equiped armor on death. Neutral alignment will expire 2 minutes after last hit on player.");
                                p.sendMessage(ChatColor.YELLOW + "* YOU ARE NOW " + ChatColor.BOLD + "NEUTRAL" + ChatColor.YELLOW + " ALIGNMENT *");
                                ActionBarAPI.sendActionBar(p, ChatColor.YELLOW + "* YOU ARE NOW " + ChatColor.BOLD + "NEUTRAL" + ChatColor.YELLOW + " ALIGNMENT *", 60);
                            } else {
                                Alignments.chaotic.put(p.getName(), --time);
                            }
                        }
                        if (Alignments.neutral.containsKey(p.getName())) {
                            time = Alignments.neutral.get(p.getName());
                            if (time == 1) {
                                Alignments.neutral.remove(p.getName());
                                Alignments.updatePlayerAlignment(p);
                                p.sendMessage(ChatColor.GREEN + "          * YOU ARE NOW " + ChatColor.BOLD + "LAWFUL" + ChatColor.GREEN + " ALIGNMENT *");
                                p.sendMessage(ChatColor.GRAY + "While lawful, you will not lose any equipped armor on death, instead, all armor will lose 30% of its durability when you die. Any players who kill you while you're lawfully aligned will become chaotic.");
                                p.sendMessage(ChatColor.GREEN + "          * YOU ARE NOW " + ChatColor.BOLD + "LAWFUL" + ChatColor.GREEN + " ALIGNMENT *");
                                ActionBarAPI.sendActionBar(p, ChatColor.GREEN + "* YOU ARE NOW " + ChatColor.BOLD + "LAWFUL" + ChatColor.GREEN + " ALIGNMENT *", 60);
                            } else {
                                --time;
                                Alignments.neutral.put(p.getName(), time--);
                            }
                        }
                    }
                    if (Alignments.tagged.containsKey(p.getName()) && (!Alignments.tagged.containsKey(p.getName()) || System.currentTimeMillis() - Alignments.tagged.get(p.getName()) <= 10000) || p.getHealth() <= 0.0)
                        continue;
                    PlayerInventory i = p.getInventory();
                    double amt = 5.0;
                    int vit = 0;
                    ItemStack[] arritemStack = i.getArmorContents();
                    int n = arritemStack.length;
                    int n2 = 0;
                    while (n2 < n) {
                        ItemStack is = arritemStack[n2];
                        if (is != null && is.getType() != Material.AIR && is.hasItemMeta() && is.getItemMeta().hasLore()) {
                            double added = Damage.getHps(is);
                            amt += added;
                            int addedvit = Damage.getElem(is, "VIT");
                            vit += addedvit;
                        }
                        ++n2;
                    }
                    if (vit > 0) {
                        amt += (double) ((int) Math.round((double) vit * 0.3));
                    }
                    double healthToSet = p.getHealth() + amt;
                    if(healthToSet > p.getMaxHealth()) {
                        p.setHealth(p.getMaxHealth());
                    } else p.setHealth(healthToSet);
                	double healthPercentage = ((double) p.getHealth() / p.getMaxHealth());
                    if (healthPercentage * 100.0F > 100.0F) {
                        healthPercentage = 1.0;
                    }
                    float pcnt = (float) (healthPercentage * 1.F);
                    if (!playerBossBars.containsKey(p)) {
                        // Set new one
                        BossBar bossBar = Bukkit.createBossBar(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "HP " + ChatColor.LIGHT_PURPLE
                                + (int) p.getHealth() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " / "
                                + ChatColor.LIGHT_PURPLE + (int) p.getMaxHealth(), BarColor.PINK, BarStyle.SOLID);
                        bossBar.addPlayer(p);
                        playerBossBars.put(p, bossBar);
                        playerBossBars.get(p).setProgress(pcnt);
                    } else {
                        playerBossBars.get(p).setTitle(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "HP " + ChatColor.LIGHT_PURPLE
                                + (int) p.getHealth() + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " / "
                                + ChatColor.LIGHT_PURPLE + (int) p.getMaxHealth());
                        playerBossBars.get(p).setProgress(pcnt);
                    }
                }
            }
        }.runTaskTimerAsynchronously(PracticeServer.plugin, 20, 20);
        File file = new File(PracticeServer.plugin.getDataFolder(), "alignments.yml");
        YamlConfiguration config = new YamlConfiguration();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (config.getConfigurationSection("chaotic") != null) {
            for (String key : config.getConfigurationSection("chaotic").getKeys(false)) {
                time = config.getConfigurationSection("chaotic").getInt(key);
                chaotic.put(key, time);
            }
        }
        if (config.getConfigurationSection("neutral") != null) {
            for (String key : config.getConfigurationSection("neutral").getKeys(false)) {
                time = config.getConfigurationSection("neutral").getInt(key);
                neutral.put(key, time);
            }
        }
    }

    public void onDisable() {
        PracticeServer.log.info("[Alignments] has been disabled.");
        File file = new File(PracticeServer.plugin.getDataFolder(), "alignments.yml");
        YamlConfiguration config = new YamlConfiguration();
        for (String s2 : chaotic.keySet()) {
            config.set("chaotic." + s2, chaotic.get(s2));
        }
        for (String s2 : neutral.keySet()) {
            config.set("neutral." + s2, neutral.get(s2));
        }
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChaoticSpawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (chaotic.containsKey(p.getName())) {
            p.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " enter " + ChatColor.BOLD + "NON-PVP" + ChatColor.RED + " zones with a chaotic alignment.");
            e.setRespawnLocation(TeleportBooks.generateRandomSpawnPoint(p.getName()));
        } else {
            e.setRespawnLocation(TeleportBooks.Cyrennica);
        }
    }

    @EventHandler
    public void onZoneMessage(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (Alignments.isSafeZone(e.getFrom()) && chaotic.containsKey(p.getName())) {
            p.sendMessage(ChatColor.RED + "The guards have kicked you out of the " + ChatColor.UNDERLINE + "protected area" + ChatColor.RED + " due to your chaotic alignment.");
            ActionBarAPI.sendActionBar(p, ChatColor.RED + "The guards have kicked you out of the " + ChatColor.UNDERLINE + "protected area" + ChatColor.RED + " due to your chaotic alignment.", 50);
            p.teleport(TeleportBooks.generateRandomSpawnPoint(p.getName()));
            return;
        }
        if (Alignments.isSafeZone(e.getTo())) {
            if (chaotic.containsKey(p.getName())) {
                p.teleport(e.getFrom());
                p.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " enter " + ChatColor.BOLD + "NON-PVP" + ChatColor.RED + " zones with a chaotic alignment.");
                ActionBarAPI.sendActionBar(p, ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " enter " + ChatColor.BOLD + "NON-PVP" + ChatColor.RED + " zones with a chaotic alignment.", 50);
                return;
            }
            if (Listeners.combat.containsKey(p.getName()) && System.currentTimeMillis() - Listeners.combat.get(p.getName()) <= 10000) {
                p.teleport(e.getFrom());
                long combattime = Listeners.combat.get(p.getName());
                double left = (System.currentTimeMillis() - combattime) / 1000;
                int time = (int) (10 - Math.round(left));
                p.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " leave a chaotic zone while in combat.");
                p.sendMessage(ChatColor.GRAY + "Out of combat in: " + ChatColor.BOLD + time + "s");
                ActionBarAPI.sendActionBar(p, ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " leave a chaotic zone while in combat, " + ChatColor.GRAY + "Out of combat in: " + ChatColor.BOLD + time + "s", 50);
                return;
            }
        }
        if (!Alignments.isSafeZone(e.getFrom()) && Alignments.isSafeZone(e.getTo())) {
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "         *** SAFE ZONE (DMG-OFF)***");
            TitleAPI.sendTitle(p, 20, 20, 20, ChatColor.GREEN.toString() + ChatColor.BOLD + "*** SAFE ZONE ***", ChatColor.GRAY + "(PVP-OFF) (MONSTERS-OFF)");
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.25f, 0.3f);
            

        }
        if (Alignments.isSafeZone(e.getFrom()) && !Alignments.isSafeZone(e.getTo())) {
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD +          "*** CHAOTIC ZONE (PVP-ON)***");
            TitleAPI.sendTitle(p, 20, 20, 20, ChatColor.RED.toString() + ChatColor.BOLD + "*** CHAOTIC ZONE ***", ChatColor.GRAY + "(PVP-ON) (MONSTERS-ON)");
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 0.25f, 0.3f);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleportChaotic(PlayerTeleportEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player p = e.getPlayer();
        if (Alignments.isSafeZone(e.getTo())) {
            if (chaotic.containsKey(p.getName())) {
                p.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " enter " + ChatColor.BOLD + "NON-PVP" + ChatColor.RED + " zones with a chaotic alignment.");
                ActionBarAPI.sendActionBar(p, ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " enter " + ChatColor.BOLD + "NON-PVP" + ChatColor.RED + " zones with a chaotic alignment.", 50);
                e.setCancelled(true);
                return;
            }
            if (Listeners.combat.containsKey(p.getName()) && System.currentTimeMillis() - Listeners.combat.get(p.getName()) <= 10000) {
                long combattime = Listeners.combat.get(p.getName());
                double left = (System.currentTimeMillis() - combattime) / 1000;
                int time = (int) (10 - Math.round(left));
                p.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " leave a chaotic zone while in combat.");
                p.sendMessage(ChatColor.GRAY + "Out of combat in: " + ChatColor.BOLD + time + "s");
                ActionBarAPI.sendActionBar(p, ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " leave a chaotic zone while in combat, " + ChatColor.GRAY + "Out of combat in: " + ChatColor.BOLD + time + "s", 50);
                e.setCancelled(true);
                return;
            }
        }
        if (!Alignments.isSafeZone(e.getFrom()) && Alignments.isSafeZone(e.getTo())) {
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "          *** SAFE ZONE (DMG-OFF) ***");
            TitleAPI.sendTitle(p, 20, 40, 20, ChatColor.GREEN.toString() + ChatColor.BOLD + "*** SAFE ZONE ***", ChatColor.GRAY + "(PVP-OFF) (MONSTERS-OFF)");
            p.playSound(e.getTo(), Sound.ENTITY_WITHER_SHOOT, 0.25f, 0.3f);
        }
        if (Alignments.isSafeZone(e.getFrom()) && !Alignments.isSafeZone(e.getTo())) {
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "          *** CHAOTIC ZONE (PVP-ON) ***");
            TitleAPI.sendTitle(p, 20, 40, 20, ChatColor.RED.toString() + ChatColor.BOLD + " *** CHAOTIC ZONE ***", ChatColor.GRAY + "(PVP-ON) (MONSTERS-ON)");
            p.playSound(e.getTo(), Sound.ENTITY_WITHER_SHOOT, 0.25f, 0.3f);
        }
    }

    public static boolean isSafeZone(Location loc) {
        ApplicableRegionSet locset = WGBukkit.getRegionManager((World) loc.getWorld()).getApplicableRegions(loc);
        if (locset.queryState(null, new StateFlag[]{DefaultFlag.PVP}) == StateFlag.State.DENY) {
            return true;
        }
        return false;
    }

    public static void updatePlayerAlignment(Player p) {
        ChatColor cc = ChatColor.GRAY;
        cc = p.getName().equalsIgnoreCase("RedsEmporium") ? ChatColor.GOLD : (p.isOp() ? ChatColor.AQUA : (neutral.containsKey(p.getName()) ? ChatColor.YELLOW : (chaotic.containsKey(p.getName()) ? ChatColor.RED : ChatColor.GRAY)));
        p.setDisplayName(String.valueOf(Alignments.getPlayerPrefix(p)) + cc + p.getName());
        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 10.0f, 1.0f);
        Scoreboards.updateAllColors();
    }

    static String getPlayerPrefix(Player p) {
        String prefix = "";
        String rank = "";
        if (Setrank.ranks.containsKey(p.getName())) {
            rank = Setrank.ranks.get(p.getName());
        }
        if (rank.equals("sub")) {
            prefix = ChatColor.GREEN.toString() + ChatColor.BOLD + "S ";
        }
        if (rank.equals("sub+")) {
            prefix = ChatColor.GOLD.toString() + ChatColor.BOLD + "S+ ";
        }
        if (rank.equals("sub++")) {
            prefix = ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "S++ ";
        }
        if (rank.equals("pmod")) {
            prefix = ChatColor.WHITE.toString() + ChatColor.BOLD + "PMOD ";
        }
        if (p.getName().equals("RedsEmporium")) {
            prefix = ChatColor.GOLD.toString() + ChatColor.BOLD + "DEV ";
        }
        if (p.isOp() && !p.getName().equals("RedsEmporium")) {
            prefix = ChatColor.AQUA.toString() + ChatColor.BOLD + "GM ";

        }
        return prefix;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Alignments.updatePlayerAlignment(p);
        Scoreboards.updatePlayerHealth();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNeutral(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player d = (Player) e.getDamager();
            if (e.getDamage() <= 0.0) {
                return;
            }
            if (!chaotic.containsKey(d.getName())) {
                if (neutral.containsKey(d.getName())) {
                    neutral.put(d.getName(), 120);
                } else {
                    d.sendMessage(ChatColor.YELLOW + "          * YOU ARE NOW " + ChatColor.BOLD + "NEUTRAL" + ChatColor.YELLOW + " ALIGNMENT *");
                    d.sendMessage(ChatColor.GRAY + "While neutral, players who kill you will not become chaotic. You have a 50% chance of dropping your weapon, and a 25% chance of dropping each piece of equiped armor on death. Neutral alignment will expire 2 minutes after last hit on player.");
                    d.sendMessage(ChatColor.YELLOW + "          * YOU ARE NOW " + ChatColor.BOLD + "NEUTRAL" + ChatColor.YELLOW + " ALIGNMENT *");
                    ActionBarAPI.sendActionBar(d, ChatColor.YELLOW + "* YOU ARE NOW " + ChatColor.BOLD + "NEUTRAL" + ChatColor.YELLOW + " ALIGNMENT *", 60);
                    neutral.put(d.getName(), 120);
                    Alignments.updatePlayerAlignment(d);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChaotic(PlayerDeathEvent e) {
        int time;
        Player p = e.getEntity();
        if (!Damage.lastphit.containsKey(p)) {
            return;
        }
        if (Damage.lasthit.containsKey(p) && System.currentTimeMillis() - Damage.lasthit.get(p) > 8000) {
            return;
        }
        Player d = Damage.lastphit.get(p);
        if (!neutral.containsKey(p.getName()) && !chaotic.containsKey(p.getName())) {
            if (chaotic.containsKey(d.getName())) {
                time = chaotic.get(d.getName());
                chaotic.put(d.getName(), time + 600);
                d.sendMessage("\u00a7cLAWFUL player slain, \u00a7l+600s \u00a7cadded to Chaotic timer");
                neutral.remove(d.getName());
                Alignments.updatePlayerAlignment(d);
            } else {
                d.sendMessage(ChatColor.RED + "          * YOU ARE NOW " + ChatColor.BOLD + "CHAOTIC" + ChatColor.RED + " ALIGNMENT *");
                d.sendMessage(ChatColor.GRAY + "While chaotic, you cannot enter any major cities or safe zones. If you are killed while chaotic, you will lose everything in your inventory. Chaotic alignment will expire 10 minutes after your last player kill.");
                d.sendMessage(ChatColor.RED + "          * YOU ARE NOW " + ChatColor.BOLD + "CHAOTIC" + ChatColor.RED + " ALIGNMENT *");
                d.sendMessage(ChatColor.RED + "LAWFUL player slain, " + ChatColor.BOLD + "+600s" + ChatColor.RED + " added to Chaotic timer.");
                ActionBarAPI.sendActionBar(d, ChatColor.RED + "* YOU ARE NOW " + ChatColor.BOLD + "CHAOTIC" + ChatColor.RED + " ALIGNMENT *", 60);
                chaotic.put(d.getName(), 600);
                neutral.remove(d.getName());
                Alignments.updatePlayerAlignment(d);
            }
        }
        if (neutral.containsKey(p.getName()) && !chaotic.containsKey(p.getName()) && chaotic.containsKey(d.getName())) {
            time = chaotic.get(d.getName());
            chaotic.put(d.getName(), time + 300);
            d.sendMessage(ChatColor.RED + "NEUTRAL player slain, " + ChatColor.BOLD + "+300s" + ChatColor.RED + " added to Chaotic timer.");
            neutral.remove(d.getName());
            Alignments.updatePlayerAlignment(d);
        }
        if (chaotic.containsKey(p.getName()) && chaotic.containsKey(d.getName())) {
            time = chaotic.get(d.getName());
            if (time <= 300) {
                chaotic.remove(d.getName());
                neutral.put(d.getName(), 120);
                Alignments.updatePlayerAlignment(d);
                d.sendMessage("\u00a7cCHAOTIC player slain, \u00a7l-300s \u00a7ctaken to Chaotic timer");
                d.sendMessage(ChatColor.YELLOW + "          * YOU ARE NOW " + ChatColor.BOLD + "NEUTRAL" + ChatColor.YELLOW + " ALIGNMENT *");
                d.sendMessage(ChatColor.GRAY + "While neutral, players who kill you will not become chaotic. You have a 50% chance of dropping your weapon, and a 25% chance of dropping each piece of equiped armor on death. Neutral alignment will expire 2 minutes after last hit on player.");
                d.sendMessage(ChatColor.YELLOW + "          * YOU ARE NOW " + ChatColor.BOLD + "NEUTRAL" + ChatColor.YELLOW + " ALIGNMENT *");
            } else {
                chaotic.put(d.getName(), time -= 300);
                d.sendMessage(ChatColor.GREEN + "Chaotic player slain, " + ChatColor.BOLD + "-300s" + ChatColor.GREEN + " removed from Chatoic timer.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeathMessage(PlayerDeathEvent e) {
        Player p = e.getEntity();
        String reason = " has died";
        if (p.getLastDamageCause() != null && p.getLastDamageCause().getCause() != null) {
            EntityDamageByEntityEvent et;
            if (p.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.LAVA) || p.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.FIRE) || p.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                reason = " burned to death";
            }
            if (p.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.SUICIDE)) {
                reason = " ended their own life";
            }
            if (p.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                reason = " fell to their death";
            }
            if (p.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
                reason = " was crushed to death";
            }
            if (p.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
                reason = " drowned to death";
            }
            if (p.getLastDamageCause() instanceof EntityDamageByEntityEvent && (et = (EntityDamageByEntityEvent) p.getLastDamageCause()).getDamager() instanceof LivingEntity) {
                if (et.getDamager() instanceof Player) {
                    Player d = (Player) et.getDamager();
                    ItemStack wep = d.getItemInHand();
                    if (Staffs.staff.containsKey(d)) {
                        wep = Staffs.staff.get(d);
                    }
                    String wepname = "";
                    wepname = wep != null && wep.getType() != Material.AIR && wep.getItemMeta().hasDisplayName() ? wep.getItemMeta().getDisplayName() : String.valueOf(wep.getType().name().substring(0, 1).toUpperCase()) + wep.getType().name().substring(1, wep.getType().name().length()).toLowerCase();
                    reason = " was killed by " + ChatColor.RESET + d.getDisplayName() + ChatColor.WHITE + " with a(n) " + wepname;
                } else if (et.getDamager() instanceof LivingEntity) {
                    LivingEntity l = (LivingEntity) et.getDamager();
                    String name = "";
                    if (l.hasMetadata("name")) {
                        name = ((MetadataValue) l.getMetadata("name").get(0)).asString();
                    }
                    reason = " was killed by a(n) " + ChatColor.UNDERLINE + name;
                }
            }
            if (Damage.lastphit.containsKey(p) && (!Damage.lasthit.containsKey(p) || System.currentTimeMillis() - Damage.lasthit.get(p) <= 8000)) {
                Player d = Damage.lastphit.get(p);
                ItemStack wep = d.getItemInHand();
                if (Staffs.staff.containsKey(d)) {
                    wep = Staffs.staff.get(d);
                }
                String wepname = "";
                wepname = wep != null && wep.getType() != Material.AIR && wep.getItemMeta().hasDisplayName() ? wep.getItemMeta().getDisplayName() : String.valueOf(wep.getType().name().substring(0, 1).toUpperCase()) + wep.getType().name().substring(1, wep.getType().name().length()).toLowerCase();
                reason = " was killed by " + ChatColor.RESET + d.getDisplayName() + ChatColor.WHITE + " with a(n) " + wepname;
            }
        }
        p.sendMessage(String.valueOf(p.getDisplayName()) + ChatColor.WHITE + reason);
        for (Entity near : p.getNearbyEntities(50.0, 50.0, 50.0)) {
            if (!(near instanceof Player)) continue;
            ((Player) near).sendMessage(String.valueOf(p.getDisplayName()) + ChatColor.WHITE + reason);
        }
    }

}

