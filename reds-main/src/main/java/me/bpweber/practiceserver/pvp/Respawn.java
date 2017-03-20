package me.bpweber.practiceserver.pvp;

import me.bpweber.practiceserver.*;
import me.bpweber.practiceserver.item.*;
import me.bpweber.practiceserver.player.*;
import me.bpweber.practiceserver.teleport.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;
import org.bukkit.scheduler.*;

import java.io.*;
import java.util.*;

public class Respawn implements Listener {
    List<Player> dead;
    Listeners lis = new Listeners();

    public Respawn() {
        this.dead = new ArrayList<Player>();
    }

    public void onEnable() {
        PracticeServer.log.info("[Respawn] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
        final File file = new File(PracticeServer.plugin.getDataFolder(), "respawndata");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void onDisable() {
        PracticeServer.log.info("[Respawn] has been disabled.");
        final File file = new File(PracticeServer.plugin.getDataFolder(), "respawndata");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(final PlayerDeathEvent e) {
        final Player p = e.getEntity();
        if (!this.dead.contains(p)) {
            this.dead.add(p);
            final Random random = new Random();
            final int wepdrop = random.nextInt(2) + 1;
            final int armor = random.nextInt(4) + 1;
            final List<ItemStack> newInventory = new ArrayList<ItemStack>();
            if (!Alignments.neutral.containsKey(p.getName()) && !Alignments.chaotic.containsKey(p.getName())) {
                ItemStack[] armorContents;
                for (int length = (armorContents = p.getInventory().getArmorContents()).length, j = 0; j < length; ++j) {
                    final ItemStack is = armorContents[j];
                    if (is != null && is.getType() != Material.AIR) {
                        if (Durability.getDuraPercent(is) > 30.0f) {
                            newInventory.add(Durability.takeDura(is, 30.0f));
                        } else {
                            e.getDrops().remove(is);
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        }
                    }
                }
                if (p.getInventory().getItem(0) != null && !p.getInventory().getItem(0).getType().name().contains("_PICKAXE")) {
                    final ItemStack is = p.getInventory().getItem(0);
                    if (is.getType().name().contains("_SWORD") || is.getType().name().contains("_AXE") || is.getType().name().contains("_SPADE") || is.getType().name().contains("_HOE") || is.getType().name().contains("_HELMET") || is.getType().name().contains("_CHESTPLATE") || is.getType().name().contains("_BOOTS")) {
                        if (Durability.getDuraPercent(is) > 30.0f) {
                            newInventory.add(Durability.takeDura(is, 30.0f));
                        } else {
                            e.getDrops().remove(is);
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        }
                    } else {
                        newInventory.add(is);
                    }
                }
                ItemStack[] contents;
                for (int length2 = (contents = p.getInventory().getContents()).length, k = 0; k < length2; ++k) {
                    final ItemStack is = contents[k];
                    if (is != null && is.getType() != Material.AIR && is.getType().name().contains("_PICKAXE")) {
                        if (Durability.getDuraPercent(is) > 30.0f) {
                            newInventory.add(Durability.takeDura(is, 30.0f));
                        } else {
                            e.getDrops().remove(is);
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        }
                    }
                }
            } else if (Alignments.neutral.containsKey(p.getName()) && !Alignments.chaotic.containsKey(p.getName())) {
                final List<ItemStack> arm = new ArrayList<ItemStack>();
                ItemStack[] armorContents2;
                for (int length3 = (armorContents2 = p.getInventory().getArmorContents()).length, l = 0; l < length3; ++l) {
                    final ItemStack is2 = armorContents2[l];
                    if (is2 != null && is2.getType() != Material.AIR) {
                        arm.add(is2);
                    }
                }
                if (armor == 1 && arm.size() > 0) {
                    arm.remove(arm.get(random.nextInt(arm.size())));
                }
                if (arm.size() > 0) {
                    for (final ItemStack is2 : arm) {
                        if (Durability.getDuraPercent(is2) > 30.0f) {
                            newInventory.add(Durability.takeDura(is2, 30.0f));
                        } else {
                            e.getDrops().remove(is2);
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        }
                    }
                }
                if (wepdrop == 1 && p.getInventory().getItem(0) != null && !p.getInventory().getItem(0).getType().name().contains("_PICKAXE")) {
                    final ItemStack is2 = p.getInventory().getItem(0);
                    if (is2.getType().name().contains("_SWORD") || is2.getType().name().contains("_AXE") || is2.getType().name().contains("_SPADE") || is2.getType().name().contains("_HOE") || is2.getType().name().contains("_HELMET") || is2.getType().name().contains("_CHESTPLATE") || is2.getType().name().contains("_BOOTS")) {
                        if (Durability.getDuraPercent(is2) > 30.0f) {
                            newInventory.add(Durability.takeDura(is2, 30.0f));
                        } else {
                            e.getDrops().remove(is2);
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        }
                    } else {
                        newInventory.add(is2);
                    }
                }
                ItemStack[] contents2;
                for (int length4 = (contents2 = p.getInventory().getContents()).length, n = 0; n < length4; ++n) {
                    final ItemStack is2 = contents2[n];
                    if (is2 != null && is2.getType() != Material.AIR && is2.getType().name().contains("_PICKAXE")) {
                        if (Durability.getDuraPercent(is2) > 30.0f) {
                            newInventory.add(Durability.takeDura(is2, 30.0f));
                        } else {
                            e.getDrops().remove(is2);
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                        }
                    }
                }
            }
            ItemStack[] contents3;
            for (int length5 = (contents3 = p.getInventory().getContents()).length, n2 = 0; n2 < length5; ++n2) {
                final ItemStack is = contents3[n2];
                if (is != null && is.getType() != Material.AIR && is.getItemMeta().hasLore() && is.getItemMeta().getLore().contains(ChatColor.GRAY + "Permenant Untradeable") && !newInventory.contains(is)) {
                    newInventory.add(is);
                }
            }
            final File file = new File(PracticeServer.plugin.getDataFolder() + "/respawndata", String.valueOf(p.getName()) + ".yml");
            final YamlConfiguration config = new YamlConfiguration();
            for (int i = 0; i < newInventory.size(); ++i) {
                config.set(new StringBuilder().append(i).toString(), (Object) newInventory.get(i));
            }
            try {
                config.save(file);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            for (final ItemStack is3 : newInventory) {
                if (is3 != null) {
                    final ItemMeta meta = is3.getItemMeta();
                    meta.setLore((List<String>) Arrays.asList("notarealitem"));
                    is3.setItemMeta(meta);
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        if (this.dead.contains(p)) {
            this.dead.remove(p);
        }
        final File file = new File(PracticeServer.plugin.getDataFolder() + "/respawndata", String.valueOf(p.getName()) + ".yml");
        final YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        for (int i = 0; i < p.getInventory().getSize(); ++i) {
            if (config.contains(new StringBuilder().append(i).toString())) {
                e.getPlayer().getInventory().addItem(new ItemStack[]{config.getItemStack(new StringBuilder().append(i).toString())});
            }
        }
        lis.Kit(p);
        final ItemStack bread = new ItemStack(Material.BREAD);
        final ItemMeta breadmeta = bread.getItemMeta();
        breadmeta.setLore((List<String>) Arrays.asList(ChatColor.GRAY + "Untradeable"));
        bread.setItemMeta(breadmeta);
        for (int t2 = 0; t2 < 2; ++t2) {
            p.getInventory().setItem(p.getInventory().firstEmpty(), bread);
        }
        if (!p.getInventory().contains(Material.QUARTZ)) {
            p.getInventory().addItem(new ItemStack[]{Hearthstone.hearthstone()});
        }
        if (!p.getInventory().contains(Material.WRITTEN_BOOK)) {
            p.getInventory().addItem(new ItemStack[]{Journal.journal()});
        }
        e.getPlayer().setMaxHealth(50.0);
        e.getPlayer().setHealth(50.0);
        p.setLevel(100);
        p.setExp(1.0f);
        p.getInventory().setHeldItemSlot(0);
        new BukkitRunnable() {
            public void run() {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 1));
            }
        }.runTaskLater(PracticeServer.plugin, 1L);
    }
}
