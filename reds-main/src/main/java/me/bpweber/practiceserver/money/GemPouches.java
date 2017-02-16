/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package me.bpweber.practiceserver.money;

import me.bpweber.practiceserver.PracticeServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class GemPouches
        implements Listener {
    public void onEnable() {
        PracticeServer.log.info("[GemPouches] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
    }

    public void onDisable() {
        PracticeServer.log.info("[GemPouches] has been disabled.");
    }

    public static ItemStack gemPouch(int tier) {
        String name = "";
        String lore = "";
        if (tier == 1) {
            name = (Object) ChatColor.WHITE + "Small Gem Pouch" + (Object) ChatColor.GREEN + (Object) ChatColor.BOLD + " 0g";
            lore = (Object) ChatColor.GRAY + "A small linen pouch that holds " + (Object) ChatColor.BOLD + "100g";
        }
        if (tier == 2) {
            name = (Object) ChatColor.GREEN + "Medium Gem Sack" + (Object) ChatColor.GREEN + (Object) ChatColor.BOLD + " 0g";
            lore = (Object) ChatColor.GRAY + "A medium wool sack that holds " + (Object) ChatColor.BOLD + "150g";
        }
        if (tier == 3) {
            name = (Object) ChatColor.AQUA + "Large Gem Satchel" + (Object) ChatColor.GREEN + (Object) ChatColor.BOLD + " 0g";
            lore = (Object) ChatColor.GRAY + "A large leather satchel that holds " + (Object) ChatColor.BOLD + "200g";
        }
        if (tier == 4) {
            name = (Object) ChatColor.LIGHT_PURPLE + "Gigantic Gem Container" + (Object) ChatColor.GREEN + (Object) ChatColor.BOLD + " 0g";
            lore = (Object) ChatColor.GRAY + "A giant container that holds " + (Object) ChatColor.BOLD + "300g";
        }
        if (tier == 6) {
            name = (Object) ChatColor.RED + "Insane Gem Container" + (Object) ChatColor.GREEN + (Object) ChatColor.BOLD + " 0g";
            lore = (Object) ChatColor.GRAY + "A giant container that holds " + (Object) ChatColor.BOLD + "100000g";
        }
        ItemStack is = new ItemStack(Material.INK_SACK);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return is;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().equals("container.crafting")) {
            int amt;
            if (e.isLeftClick() && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.INK_SACK && GemPouches.isGemPouch(e.getCurrentItem()) && e.getCursor() != null && e.getCursor().getType() == Material.EMERALD) {
                if (e.getCurrentItem().getAmount() != 1) {
                    return;
                }
                e.setCancelled(true);
                amt = GemPouches.getCurrentValue(e.getCurrentItem());
                int max = GemPouches.getMaxValue(e.getCurrentItem());
                int add = e.getCursor().getAmount();
                if (amt < max) {
                    if (amt + add > max) {
                        e.getCursor().setAmount(add - (max - amt));
                        GemPouches.setPouchBal(e.getCurrentItem(), max);
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    } else {
                        e.setCursor(null);
                        GemPouches.setPouchBal(e.getCurrentItem(), amt + add);
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                    }
                }
            }
            if (e.isRightClick() && e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.INK_SACK && GemPouches.isGemPouch(e.getCurrentItem()) && (e.getCursor() == null || e.getCursor().getType() == Material.AIR)) {
                if (e.getCurrentItem().getAmount() != 1) {
                    return;
                }
                e.setCancelled(true);
                amt = GemPouches.getCurrentValue(e.getCurrentItem());
                if (amt <= 0) {
                    return;
                }
                if (amt > 64) {
                    e.setCursor(Money.makeGems(64));
                    GemPouches.setPouchBal(e.getCurrentItem(), amt -= 64);
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                } else {
                    e.setCursor(Money.makeGems(amt));
                    GemPouches.setPouchBal(e.getCurrentItem(), 0);
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemPickup(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (e.isCancelled()) {
            return;
        }
        if (e.getItem().getItemStack().getType() != Material.EMERALD) {
            return;
        }
        int add = e.getItem().getItemStack().getAmount();
        ItemStack[] arritemStack = p.getInventory().getContents();
        int n = arritemStack.length;
        int n2 = 0;
        while (n2 < n) {
            ItemStack is = arritemStack[n2];
            if (is != null && GemPouches.isGemPouch(is)) {
                if (is.getAmount() != 1) {
                    return;
                }
                int amt = GemPouches.getCurrentValue(is);
                int max = GemPouches.getMaxValue(is);
                if (add > 0 && amt < max) {
                    if (amt + add > max) {
                        ItemStack newis = e.getItem().getItemStack();
                        newis.setAmount(add -= max - amt);
                        e.getItem().setItemStack(newis);
                        GemPouches.setPouchBal(is, max);
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        e.setCancelled(true);
                        int adding = max - amt;
                        p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "                    +" + ChatColor.GREEN.toString() + adding + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "G");
                    } else {
                        e.getItem().remove();
                        GemPouches.setPouchBal(is, amt + add);
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "                    +" + ChatColor.GREEN.toString() + add + ChatColor.GREEN + ChatColor.BOLD + "G");
                        add = 0;
                    }
                }
            }
            ++n2;
        }
    }

    static int getMaxValue(ItemStack is) {
        List<String> lore;
        if (is != null && is.getType() != Material.AIR && is.getType() == Material.INK_SACK && is.getItemMeta().hasLore() && (lore = is.getItemMeta().getLore()).size() > 0 && ((String) lore.get(0)).contains("g")) {
            try {
                String line = ChatColor.stripColor((String) ((String) lore.get(0)));
                return Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1, line.lastIndexOf("g")));
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    static int getCurrentValue(ItemStack is) {
        if (is != null && is.getType() != Material.AIR && is.getType() == Material.INK_SACK && is.getItemMeta().hasDisplayName()) {
            try {
                String line = ChatColor.stripColor((String) is.getItemMeta().getDisplayName());
                return Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1, line.lastIndexOf("g")));
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    static void setPouchBal(ItemStack is, int bal) {
        if (is.getItemMeta().hasDisplayName()) {
            String name = is.getItemMeta().getDisplayName();
            name = name.substring(0, name.lastIndexOf(" "));
            name = String.valueOf(name) + " " + bal + "g";
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(name);
            is.setItemMeta(im);
        }
    }

    public static boolean isGemPouch(ItemStack is) {
        if (is == null) {
            return false;
        }
        if (is.getType() != Material.INK_SACK) {
            return false;
        }
        if (is.getDurability() != 0) {
            return false;
        }
        if (!is.getItemMeta().hasDisplayName()) {
            return false;
        }
        if (!is.getItemMeta().getDisplayName().contains("g")) {
            return false;
        }
        if (GemPouches.getMaxValue(is) == 0) {
            return false;
        }
        return true;
    }
}

