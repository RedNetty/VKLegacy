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
 *  org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 *  org.bukkit.util.Vector
 */
package me.bpweber.practiceserver.teleport;

import me.bpweber.practiceserver.*;
import me.bpweber.practiceserver.money.*;
import me.bpweber.practiceserver.player.*;
import me.bpweber.practiceserver.pvp.*;
import me.bpweber.practiceserver.utils.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_9_R2.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.*;
import org.bukkit.scheduler.*;
import org.inventivetalent.glow.GlowAPI;

import java.util.*;

public class TeleportBooks
        implements Listener {
    public static Location Cyrennica;
    public static Location Harrison_Field;
    public static Location Dark_Oak_Tavern;
    public static Location Deadpeaks_Mountain_Camp;
    public static Location Trollsbane_Tavern;
    public static Location Tripoli;
    public static Location Gloomy_Hollows;
    public static Location Crestguard_Keep;
    public static Location CrestWatch;
    static HashMap<String, Location> teleporting_loc;
    static HashMap<String, Location> casting_loc;
    static HashMap<String, Integer> casting_time;

    static {
        teleporting_loc = new HashMap<String, Location>();
        casting_loc = new HashMap<String, Location>();
        casting_time = new HashMap<String, Integer>();
    }

    public void onEnable() {
        Cyrennica = new Location(Bukkit.getWorlds().get(0), -367.0, 83.0, 390.0);
        Harrison_Field = new Location(Bukkit.getWorlds().get(0), -594.0, 58.0, 687.0, 92.0f, 1.0f);
        Dark_Oak_Tavern = new Location(Bukkit.getWorlds().get(0), 280.0, 58.0, 1132.0, 2.0f, 1.0f);
        Deadpeaks_Mountain_Camp = new Location(Bukkit.getWorlds().get(0), -1173.0, 105.0, 1030.0, -88.0f, 1.0f);
        Trollsbane_Tavern = new Location(Bukkit.getWorlds().get(0), 962.0, 94.0, 1069.0, -153.0f, 1.0f);
        Tripoli = new Location(Bukkit.getWorlds().get(0), -1320.0, 90.0, 370.0, 153.0f, 1.0f);
        Gloomy_Hollows = new Location(Bukkit.getWorlds().get(0), -590.0, 43.0, 0.0, 144.0f, 1.0f);
        Crestguard_Keep = new Location(Bukkit.getWorlds().get(0), -1428.0, 115.0, -489.0, 95.0f, 1.0f);
        CrestWatch = new Location(Bukkit.getWorlds().get(0), -544.0, 60.0, -418.0, 95.0f, 1.0f);
        PracticeServer.log.info("[TeleportBooks] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (!TeleportBooks.casting_time.containsKey(p.getName())) continue;
                    if (TeleportBooks.casting_time.get(p.getName()) == 0) {
                        Particles.SPELL_WITCH.display(0.0f, 0.0f, 0.0f, 0.2f, 200, p.getLocation().add(0.0, 1.0, 0.0), 20.0);
                        p.eject();
                        p.teleport(TeleportBooks.teleporting_loc.get(p.getName()));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 2));
                        TeleportBooks.casting_time.remove(p.getName());
                        TeleportBooks.casting_loc.remove(p.getName());
                        TeleportBooks.teleporting_loc.remove(p.getName());
                        continue;
                    }
                    p.sendMessage(ChatColor.BOLD + "CASTING" + ChatColor.WHITE + " ... " + TeleportBooks.casting_time.get(p.getName()) + ChatColor.BOLD + "s");
                    TeleportBooks.casting_time.put(p.getName(), TeleportBooks.casting_time.get(p.getName()) - 1);
                    Particles.PORTAL.display(0.0f, 0.0f, 0.0f, 4.0f, 300, p.getLocation(), 20.0);
                    p.getWorld().playEffect(p.getLocation().add(0, 0, 0), Effect.STEP_SOUND, Material.PORTAL);
                    p.getWorld().playEffect(p.getLocation().add(0, 1, 0), Effect.STEP_SOUND, Material.PORTAL);
                    p.getWorld().playEffect(p.getLocation().add(0, 2, 0), Effect.STEP_SOUND, Material.PORTAL);
                }
            }
        }.runTaskTimer(PracticeServer.plugin, 20, 20);
    }

    public void onDisable() {
        PracticeServer.log.info("[TeleportBooks] has been disabled.");
    }

    public static ItemStack cyrennica_book(boolean inshop) {

        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Teleport:" + ChatColor.WHITE + " Cyrennica");
        if (inshop == false) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the grand City of Cyrennica."));
        } else if (inshop == true) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the grand City of Cyrennica.", ChatColor.GREEN + "Price: " + ChatColor.WHITE + "50g"));
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack harrison_book(boolean inshop) {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Teleport:" + ChatColor.WHITE + " Harrison Field");
        if (inshop == false) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to Harrison Field."));
        } else if (inshop == true) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to Harrison Field.", ChatColor.GREEN + "Price: " + ChatColor.WHITE + "175g"));
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack dark_oak_book(boolean inshop) {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Teleport:" + ChatColor.WHITE + " Dark Oak Tavern");
        if (inshop == false) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the tavern in Dark Oak Forest."));
        } else if (inshop == true) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the tavern in Dark Oak Forest.", ChatColor.GREEN + "Price: " + ChatColor.WHITE + "200g"));

        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack deadpeaks_book(boolean inshop) {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Teleport:" + ChatColor.WHITE + " Deadpeaks Mountain Camp");
        if (inshop == false) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the Deadpeaks.", ChatColor.RED.toString() + ChatColor.BOLD + "WARNING:" + ChatColor.RED.toString() + " CHAOTIC ZONE"));
        } else if (inshop == true) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the Deadpeaks.", ChatColor.RED.toString() + ChatColor.BOLD + "WARNING:" + ChatColor.RED.toString() + " CHAOTIC ZONE", ChatColor.GREEN + "Price: " + ChatColor.WHITE + "220g"));
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack trollsbane_book(boolean inshop) {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Teleport:" + ChatColor.WHITE + " Trollsbane Tavern");
        if (inshop == false) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to Trollsbane Tavern."));
        } else if (inshop == true) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to Trollsbane Tavern.", ChatColor.GREEN + "Price: " + ChatColor.WHITE + "220g"));
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack tripoli_book(boolean inshop) {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Teleport:" + ChatColor.WHITE + " Tripoli");
        if (inshop == false) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to Tripoli."));
        } else if (inshop == true) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to Tripoli.", ChatColor.GREEN + "Price: " + ChatColor.WHITE + "220g"));
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack gloomy_book(boolean inshop) {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Teleport:" + ChatColor.WHITE + " Gloomy Hollows");
        if (inshop == false) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the Gloomy Hollows."));
        } else if (inshop == true) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the Gloomy Hollows.", ChatColor.GREEN + "Price: " + ChatColor.WHITE + "150g"));
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack crestguard_book(boolean inshop) {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Teleport:" + ChatColor.WHITE + " Crestguard Keep");
        if (inshop == false) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the Crestguard Keep."));
        }
        if (inshop == true) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the Crestguard Keep.", ChatColor.GREEN + "Price: " + ChatColor.WHITE + "400g"));
        }
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack crestwatch_book(boolean inshop) {
        ItemStack is = new ItemStack(Material.BOOK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Teleport:" + ChatColor.WHITE + " CrestWatch");
        if (inshop == false) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the CrestWatch."));
        }
        if (inshop == true) {
            im.setLore(Arrays.asList(ChatColor.GRAY + "Teleports the user to the CrestWatch.", ChatColor.GREEN + "Price: " + ChatColor.WHITE + "200g"));
        }
        is.setItemMeta(im);
        return is;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() == Material.BOOK && p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName() && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().toLowerCase().contains("teleport:") && !casting_time.containsKey(p.getName()) && !Horses.mounting.containsKey(p.getName())) {
            String type = ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
            Location loc = this.getLocationFromString(type);
            if (Alignments.chaotic.containsKey(p.getName())) {
                p.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " teleport to non-chaotic zones while chaotic.");
                p.sendMessage(ChatColor.GRAY + "Neutral in " + ChatColor.BOLD + Alignments.chaotic.get(p.getName()) + "s");
            } else {
                if (p.getInventory().getItemInMainHand().getAmount() > 1) {
                    p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
                } else {
                    p.getInventory().setItemInMainHand(null);
                }
                int seconds = 5;
                if (Alignments.tagged.containsKey(p.getName()) && System.currentTimeMillis() - Alignments.tagged.get(p.getName()) < 10000) {
                    seconds = 10;
                }
                p.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "CASTING " + ChatColor.WHITE + this.getTeleportMessage(type) + " ... " + seconds + ChatColor.BOLD + "s");
                teleporting_loc.put(p.getName(), loc);
                casting_loc.put(p.getName(), p.getLocation());
                casting_time.put(p.getName(), seconds);
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (seconds + 3) * 20, 1));
                p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void onCancelDamager(EntityDamageByEntityEvent e) {
        Player p;
        if (e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity && casting_time.containsKey((p = (Player) e.getDamager()).getName())) {
            casting_time.remove(p.getName());
            casting_loc.remove(p.getName());
            teleporting_loc.remove(p.getName());
            p.sendMessage(ChatColor.RED + "Teleportation - " + ChatColor.BOLD + "CANCELLED");
            p.removePotionEffect(PotionEffectType.CONFUSION);
        }
    }

    @EventHandler
    public void onCancelDamage(EntityDamageEvent e) {
        Player p;
        if (e.getDamage() <= 0.0) {
            return;
        }
        if (e.getEntity() instanceof Player && casting_time.containsKey((p = (Player) e.getEntity()).getName())) {
            casting_time.remove(p.getName());
            casting_loc.remove(p.getName());
            teleporting_loc.remove(p.getName());
            p.sendMessage(ChatColor.RED + "Teleportation - " + ChatColor.BOLD + "CANCELLED");
            p.removePotionEffect(PotionEffectType.CONFUSION);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (casting_time.containsKey(p.getName())) {
            casting_time.remove(p.getName());
            casting_loc.remove(p.getName());
            teleporting_loc.remove(p.getName());
        }
    }

    @EventHandler
    public void onCancelMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (casting_time.containsKey(p.getName()) && (casting_loc.get(p.getName())).distanceSquared(e.getTo()) >= 2.0) {
            casting_time.remove(p.getName());
            casting_loc.remove(p.getName());
            teleporting_loc.remove(p.getName());
            p.sendMessage(ChatColor.RED + "Teleportation - " + ChatColor.BOLD + "CANCELLED");
            p.removePotionEffect(PotionEffectType.CONFUSION);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (e.isCancelled()) {
            return;
        }
        if (e.getItem().getItemStack().getType() == Material.BOOK || e.getItem().getItemStack().getType() == Material.PAPER || e.getItem().getItemStack().getType() == Material.INK_SACK && e.getItem().getItemStack().getDurability() == 0) {
            e.setCancelled(true);
            if (p.getInventory().firstEmpty() != -1) {
                int amount = e.getItem().getItemStack().getAmount();
                CraftItemStack scroll = CraftItemStack.asCraftCopy(e.getItem().getItemStack());
                scroll.setAmount(1);
                while (amount > 0 && p.getInventory().firstEmpty() != -1) {
                    p.getInventory().setItem(p.getInventory().firstEmpty(), scroll);
                    p.updateInventory();
                    if (--amount <= 0) continue;
                    ItemStack new_stack = e.getItem().getItemStack();
                    new_stack.setAmount(amount);
                    e.getItem().setItemStack(new_stack);
                }
                if (amount <= 0) {
                    e.getItem().remove();
                }
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCursor() != null && e.getCursor().getType() == Material.BOOK && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BOOK) {
            e.setCancelled(true);
            p.updateInventory();
        }
        if (e.isShiftClick() && e.getCurrentItem() != null && (e.getCurrentItem().getType() == Material.BOOK || e.getCurrentItem().getType() == Material.PAPER || GemPouches.isGemPouch(e.getCurrentItem()))) {
            if (e.getInventory().getName().contains("Bank Chest")) {
                if (e.getRawSlot() < 63 && p.getInventory().firstEmpty() != -1) {
                    p.getInventory().setItem(p.getInventory().firstEmpty(), e.getCurrentItem());
                    e.setCurrentItem(null);
                }
                if (!GemPouches.isGemPouch(e.getCurrentItem()) && e.getRawSlot() > 53 && e.getInventory().firstEmpty() != -1) {
                    e.getInventory().setItem(e.getInventory().firstEmpty(), e.getCurrentItem());
                    e.setCurrentItem(null);
                }
            }
            e.setCancelled(true);
            p.updateInventory();
        }
        if (e.getClick() == ClickType.DOUBLE_CLICK && e.getCursor() != null && (e.getCursor().getType() == Material.BOOK || e.getCursor().getType() == Material.PAPER || GemPouches.isGemPouch(e.getCursor()))) {
            e.setCancelled(true);
            p.updateInventory();
        }
        if (e.getCursor() != null && e.getCursor().getType() == Material.PAPER && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PAPER) {
            e.setCancelled(true);
            p.updateInventory();
        }
        if (e.getCursor() != null && GemPouches.isGemPouch(e.getCursor()) && e.getCurrentItem() != null && GemPouches.isGemPouch(e.getCurrentItem())) {
            e.setCancelled(true);
            p.updateInventory();
        }
    }

    Location getLocationFromString(String s) {
        if ((s = s.toLowerCase()).contains("cyrennica")) {
            return Cyrennica;
        }
        if (s.contains("harrison field")) {
            return Harrison_Field;
        }
        if (s.contains("dark oak tavern")) {
            return Dark_Oak_Tavern;
        }
        if (s.contains("deadpeaks mountain camp")) {
            return Deadpeaks_Mountain_Camp;
        }
        if (s.contains("trollsbane tavern")) {
            return Trollsbane_Tavern;
        }
        if (s.contains("tripoli")) {
            return Tripoli;
        }
        if (s.contains("gloomy hollows")) {
            return Gloomy_Hollows;
        }
        if (s.contains("crestguard keep")) {
            return Crestguard_Keep;
        }
        if (s.contains("crestwatch")) {
            return CrestWatch;
        }
        return Cyrennica;
    }

    String getTeleportMessage(String s) {
        if ((s = s.toLowerCase()).contains("cyrennica")) {
            return "Teleport Scroll: Cyrennica";
        }
        if (s.contains("harrison field")) {
            return "Teleport Scroll: Harrison's Field";
        }
        if (s.contains("dark oak tavern")) {
            return "Teleport Scroll: Dark Oak Tavern";
        }
        if (s.contains("deadpeaks mountain camp")) {
            return "Teleport Scroll: Deadpeaks Mountain Camp";
        }
        if (s.contains("trollsbane tavern")) {
            return "Teleport Scroll: Trollsbane Tavern";
        }
        if (s.contains("tripoli")) {
            return "Teleport Scroll: Tripoli";
        }
        if (s.contains("gloomy hollows")) {
            return "Teleport Scroll: Gloomy Hollows";
        }
        if (s.contains("crestguard keep")) {
            return "Teleport Scroll: Crestguard Keep";
        }
        if (s.contains("crestwatch")) {
            return "Teleport Scroll: CrestWatch";
        }
        return "Teleport Scroll: Cyrennica";
    }

    @EventHandler
    public void onAvalonTp(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location enter = new Location(Bukkit.getWorlds().get(0), -357.5, 171.0, -3440.5);
        Location exit = new Location(Bukkit.getWorlds().get(0), -1158.5, 95.0, -515.5);
        if (to.getX() > -1155.0 && to.getX() < -1145.0 && to.getY() > 90.0 && to.getY() < 100.0 && to.getZ() < -500.0 && to.getZ() > -530.0) {
            e.getPlayer().teleport(enter.setDirection(to.getDirection()));
        }
        if (to.getX() < -360.0 && to.getX() > -370.0 && to.getY() > 165.0 && to.getY() < 190.0 && to.getZ() < -3426.0 && to.getZ() > -3455.0) {
            e.getPlayer().teleport(exit.setDirection(to.getDirection()));
        }
    }

    public static Location generateRandomSpawnPoint(String s) {
        ArrayList<Location> spawns = new ArrayList<Location>();
        if (Alignments.chaotic.containsKey(s)) {
            spawns.add(new Location(Bukkit.getWorlds().get(0), -382.0, 68.0, 867.0));
            spawns.add(new Location(Bukkit.getWorlds().get(0), -350.0, 67.0, 883.0));
            spawns.add(new Location(Bukkit.getWorlds().get(0), -330.0, 65.0, 898.0));
            spawns.add(new Location(Bukkit.getWorlds().get(0), -419.0, 61.0, 830.0));
            return spawns.get(new Random().nextInt(spawns.size()));
        }
        return Cyrennica;
    }

}

