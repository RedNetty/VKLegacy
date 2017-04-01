/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package me.bpweber.practiceserver.vendors;

import me.bpweber.practiceserver.*;
import me.bpweber.practiceserver.item.*;
import me.bpweber.practiceserver.money.*;
import me.bpweber.practiceserver.player.*;
import me.bpweber.practiceserver.teleport.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.text.*;
import java.util.*;

public class ItemVendors
        implements Listener {
    public static HashMap<String, ItemStack> buyingitem = new HashMap<String, ItemStack>();
    public static HashMap<String, Integer> buyingprice = new HashMap<String, Integer>();

    public void onEnable() {
        PracticeServer.log.info("[ItemVendors] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
    }

    public void onDisable() {
        PracticeServer.log.info("[ItemVendors] has been disabled.");
    }

    public static Integer getPriceFromLore(ItemStack is) {
        int price = 0;
        if (is != null && is.getType() != Material.AIR && is.getItemMeta().hasLore()) {
            for (String line : is.getItemMeta().getLore()) {
                if (!line.contains("Price: ")) continue;
                String val = line;
                val = ChatColor.stripColor((String) val);
                val = val.substring(7, val.length() - 1);
                try {
                    price = Integer.parseInt(val);
                    continue;
                } catch (Exception e) {
                    price = 0;
                }
            }
        }
        return price;
    }

    ItemStack food(int type) {
        ItemStack is = new ItemStack(Material.BREAD);
        int price = 2;
        if (type == 0) {
            is.setType(Material.MELON);
            price = 2;
        }
        if (type == 1) {
            is.setType(Material.APPLE);
            price = 4;
        }
        if (type == 2) {
            is.setType(Material.BREAD);
            price = 5;
        }
        if (type == 3) {
            is.setType(Material.PUMPKIN_PIE);
            price = 8;
        }
        if (type == 4) {
            is.setType(Material.COOKED_BEEF);
            price = 10;
        }
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(ChatColor.GREEN + "Price: " + ChatColor.WHITE + price + "g"));
        is.setItemMeta(im);
        return is;
    }

    @EventHandler
    public void onBankClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof HumanEntity) {
            HumanEntity p = (HumanEntity) e.getRightClicked();
            if (p.getName() == null) {
                return;
            }
            if (!p.hasMetadata("NPC")) {
                return;
            }
            if (p.getName().equals("Banker")) {
                e.getPlayer().sendMessage(ChatColor.GRAY + "Banker: " + ChatColor.WHITE + "Use these bank chests to store your precious items.");
            } else if (p.getName().equals("Item Vendor")) {
                e.getPlayer().sendMessage(ChatColor.GRAY + "Item Vendor: " + ChatColor.WHITE + "I will take your gems in return for special items.");
                Inventory inv = Bukkit.getServer().createInventory(null, 18, "Item Vendor");
                inv.addItem(new ItemStack[]{Items.orb(true)});
                inv.addItem(new ItemStack[]{Items.enchant(1, 0, true)});
                inv.addItem(new ItemStack[]{Items.enchant(1, 1, true)});
                inv.addItem(new ItemStack[]{Items.enchant(2, 0, true)});
                inv.addItem(new ItemStack[]{Items.enchant(2, 1, true)});
                inv.addItem(new ItemStack[]{Items.enchant(3, 0, true)});
                inv.addItem(new ItemStack[]{Items.enchant(3, 1, true)});
                inv.addItem(new ItemStack[]{Items.enchant(4, 0, true)});
                inv.addItem(new ItemStack[]{Items.enchant(4, 1, true)});
                inv.addItem(new ItemStack[]{Items.enchant(5, 0, true)});
                inv.addItem(new ItemStack[]{Items.enchant(5, 1, true)});
                e.getPlayer().openInventory(inv);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 1.0f);
            } else if (p.getName().equals("Fisherman")) {
                e.getPlayer().sendMessage(ChatColor.GRAY + "Fisherman: " + ChatColor.WHITE + "These fish can give you special powers.");
                Inventory inv = Bukkit.getServer().createInventory(null, 9, "Fisherman");
                inv.addItem(new ItemStack[]{Speedfish.fish(3, true)});
                inv.addItem(new ItemStack[]{Speedfish.fish(4, true)});
                inv.addItem(new ItemStack[]{Speedfish.fish(5, true)});
                e.getPlayer().openInventory(inv);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 1.0f);
            } else if (p.getName().equals("Food Vendor")) {
                Inventory inv = Bukkit.getServer().createInventory(null, 9, "Food Vendor");
                inv.addItem(new ItemStack[]{this.food(0)});
                inv.addItem(new ItemStack[]{this.food(1)});
                inv.addItem(new ItemStack[]{this.food(2)});
                inv.addItem(new ItemStack[]{this.food(3)});
                inv.addItem(new ItemStack[]{this.food(4)});
                e.getPlayer().openInventory(inv);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 1.0f);
            } else if (p.getName().equals("Book Vendor")) {
                Inventory inv = Bukkit.getServer().createInventory(null, 9, "Book Vendor");
                inv.addItem(new ItemStack[]{TeleportBooks.cyrennica_book(true)});
                inv.addItem(new ItemStack[]{TeleportBooks.harrison_book(true)});
                inv.addItem(new ItemStack[]{TeleportBooks.gloomy_book(true)});
                inv.addItem(new ItemStack[]{TeleportBooks.dark_oak_book(true)});
                inv.addItem(new ItemStack[]{TeleportBooks.trollsbane_book(true)});
                inv.addItem(new ItemStack[]{TeleportBooks.tripoli_book(true)});
                inv.addItem(new ItemStack[]{TeleportBooks.crestwatch_book(true)});
                inv.addItem(new ItemStack[]{TeleportBooks.crestguard_book(true)});
                inv.addItem(new ItemStack[]{TeleportBooks.deadpeaks_book(true)});

                e.getPlayer().openInventory(inv);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 1.0f);
            } else if (p.getName().equals("Guild God")) {
                String dayNames[] = new DateFormatSymbols().getWeekdays();
                Calendar date1 = Calendar.getInstance();
                if(dayNames[date1.get(Calendar.DAY_OF_WEEK)] != "Saturday" && dayNames[date1.get(Calendar.DAY_OF_WEEK)] != "Sunday")
                {
                    e.getPlayer().sendMessage(ChatColor.GRAY + "Guild God: " + ChatColor.WHITE + "The guild wars aren't over yet! Talk to me on Saturday or Sunday.");
                    return;
                }
                e.getPlayer().sendMessage(ChatColor.RED + "Temp. disabled!");
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equals("Item Vendor")) {
            List<String> lore;
            e.setCancelled(true);
            if (e.getCurrentItem() != null && (e.getCurrentItem().getType() == Material.MAGMA_CREAM || e.getCurrentItem().getType() == Material.EMPTY_MAP) && e.getCurrentItem().getItemMeta().hasLore() && ((String) (lore = e.getCurrentItem().getItemMeta().getLore()).get(lore.size() - 1)).contains("Price:")) {
                int price = ItemVendors.getPriceFromLore(e.getCurrentItem());
                if (Money.hasEnoughGems(p, price)) {
                    ItemStack is = new ItemStack(e.getCurrentItem().getType());
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName());
                    lore.remove(lore.size() - 1);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    buyingitem.put(p.getName(), is);
                    buyingprice.put(p.getName(), price);
                    p.sendMessage(ChatColor.GREEN + "Enter the " + ChatColor.BOLD + "QUANTITY" + ChatColor.GREEN + " you'd like to purchase.");
                    p.sendMessage(ChatColor.GRAY + "MAX: 64X (" + price * 64 + "g), OR " + price + "g/each.");
                    p.closeInventory();
                } else {
                    p.sendMessage(ChatColor.RED + "You do NOT have enough gems to purchase this " + e.getCurrentItem().getItemMeta().getDisplayName());
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "COST: " + ChatColor.RED + price + ChatColor.BOLD + "G");
                    p.closeInventory();
                }
            }
        } else if (e.getCurrentItem() != null && e.getInventory().getTitle().equals("Fisherman")) {
            List<String> lore;
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.RAW_FISH && e.getCurrentItem().getItemMeta().hasLore() && ((String) (lore = e.getCurrentItem().getItemMeta().getLore()).get(lore.size() - 1)).contains("Price:")) {
                int price = ItemVendors.getPriceFromLore(e.getCurrentItem());
                if (Money.hasEnoughGems(p, price)) {
                    ItemStack is = new ItemStack(e.getCurrentItem().getType());
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName());
                    lore.remove(lore.size() - 1);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    buyingitem.put(p.getName(), is);
                    buyingprice.put(p.getName(), price);
                    p.sendMessage(ChatColor.GREEN + "Enter the " + ChatColor.BOLD + "QUANTITY" + ChatColor.GREEN + " you'd like to purchase.");
                    p.sendMessage(ChatColor.GRAY + "MAX: 64X (" + price * 64 + "g), OR " + price + "g/each.");
                    p.closeInventory();
                } else {
                    p.sendMessage(ChatColor.RED + "You do NOT have enough gems to purchase this " + e.getCurrentItem().getItemMeta().getDisplayName());
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "COST: " + ChatColor.RED + price + ChatColor.BOLD + "G");
                    p.closeInventory();
                }
            }
        } else if (e.getCurrentItem() != null && e.getInventory().getTitle().equals("Food Vendor")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && (e.getCurrentItem().getType() == Material.MELON || e.getCurrentItem().getType() == Material.APPLE || e.getCurrentItem().getType() == Material.BREAD || e.getCurrentItem().getType() == Material.PUMPKIN_PIE || e.getCurrentItem().getType() == Material.COOKED_BEEF) && e.getCurrentItem().getItemMeta().hasLore() && ((String) (e.getCurrentItem().getItemMeta().getLore()).get(0)).contains("Price:")) {
                int price = ItemVendors.getPriceFromLore(e.getCurrentItem());
                if (Money.hasEnoughGems(p, price)) {
                    ItemStack is = new ItemStack(e.getCurrentItem().getType());
                    buyingitem.put(p.getName(), is);
                    buyingprice.put(p.getName(), price);
                    p.sendMessage(ChatColor.GREEN + "Enter the " + ChatColor.BOLD + "QUANTITY" + ChatColor.GREEN + " you'd like to purchase.");
                    p.sendMessage(ChatColor.GRAY + "MAX: 64X (" + price * 64 + "g), OR " + price + "g/each.");
                    p.closeInventory();
                } else {
                    p.sendMessage(ChatColor.RED + "You do NOT have enough gems to purchase this " + e.getCurrentItem().getType().name());
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "COST: " + ChatColor.RED + price + ChatColor.BOLD + "G");
                    p.closeInventory();
                }
            }
        } else if (e.getCurrentItem() != null && e.getInventory().getTitle().equals("Book Vendor")) {
            List<String> lore;
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.BOOK && e.getCurrentItem().getItemMeta().hasLore() && ((String) (lore = e.getCurrentItem().getItemMeta().getLore()).get(lore.size() - 1)).contains("Price:")) {
                int price = ItemVendors.getPriceFromLore(e.getCurrentItem());
                if (Money.hasEnoughGems(p, price)) {
                    ItemStack is = new ItemStack(e.getCurrentItem().getType());
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName());
                    lore.remove(lore.size() - 1);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    buyingitem.put(p.getName(), is);
                    buyingprice.put(p.getName(), price);
                    p.sendMessage(ChatColor.GREEN + "Enter the " + ChatColor.BOLD + "QUANTITY" + ChatColor.GREEN + " you'd like to purchase.");
                    p.sendMessage(ChatColor.GRAY + "MAX: 64X (" + price * 64 + "g), OR " + price + "g/each.");
                    p.closeInventory();
                } else {
                    p.sendMessage(ChatColor.RED + "You do NOT have enough gems to purchase this " + e.getCurrentItem().getItemMeta().getDisplayName());
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "COST: " + ChatColor.RED + price + ChatColor.BOLD + "G");
                    p.closeInventory();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPromptChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (buyingitem.containsKey(p.getName()) && buyingprice.containsKey(p.getName())) {
            e.setCancelled(true);
            int price = buyingprice.get(p.getName());
            ItemStack is = buyingitem.get(p.getName());
            int amt = 0;
            if (e.getMessage().equalsIgnoreCase("cancel")) {
                p.sendMessage(ChatColor.RED + "Purchase of item - " + ChatColor.BOLD + "CANCELLED");
                buyingprice.remove(p.getName());
                buyingitem.remove(p.getName());
                return;
            }
            try {
                amt = Integer.parseInt(e.getMessage());
            } catch (Exception ex) {
                p.sendMessage(ChatColor.RED + "Please enter a valid integer, or type 'cancel' to void this item purchase.");
                return;
            }
            if (amt < 1) {
                p.sendMessage(ChatColor.RED + "You cannot purchase a NON-POSITIVE number.");
                return;
            }
            if (amt > 64) {
                p.sendMessage(ChatColor.RED + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RED + " buy MORE than " + ChatColor.BOLD + "64x" + ChatColor.RED + " of a material per transaction.");
                return;
            }
            if (!Money.hasEnoughGems(p, amt * price)) {
                p.sendMessage(ChatColor.RED + "You do not have enough GEM(s) to complete this purchase.");
                p.sendMessage(ChatColor.GRAY.toString() + amt + " X " + price + " gem(s)/ea = " + amt * price + " gem(s).");
                return;
            }
            int empty = 0;
            if (is.getMaxStackSize() == 1) {
                int i = 0;
                while (i < p.getInventory().getSize()) {
                    if (p.getInventory().getItem(i) == null || p.getInventory().getItem(i).getType() == Material.AIR) {
                        ++empty;
                    }
                    ++i;
                }
                if (amt > empty) {
                    p.sendMessage(ChatColor.RED + "No space available in inventory. Type 'cancel' or clear some room.");
                } else {
                    i = 0;
                    while (i < amt) {
                        p.getInventory().setItem(p.getInventory().firstEmpty(), is);
                        ++i;
                    }
                    p.sendMessage(ChatColor.RED + "-" + amt * price + ChatColor.BOLD + "G");
                    p.sendMessage(ChatColor.GREEN + "Transaction successful.");
                    Money.takeGems(p, amt * price);
                    buyingprice.remove(p.getName());
                    buyingitem.remove(p.getName());
                }
            } else {
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage(ChatColor.RED + "No space available in inventory. Type 'cancel' or clear some room.");
                    return;
                }
                p.sendMessage(ChatColor.RED + "-" + amt * price + ChatColor.BOLD + "G");
                p.sendMessage(ChatColor.GREEN + "Transaction successful.");
                Money.takeGems(p, amt * price);
                is.setAmount(amt);
                p.getInventory().setItem(p.getInventory().firstEmpty(), is);
                buyingprice.remove(p.getName());
                buyingitem.remove(p.getName());
            }
        }
    }
}

