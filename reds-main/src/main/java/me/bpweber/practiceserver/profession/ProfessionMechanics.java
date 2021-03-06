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
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package me.bpweber.practiceserver.profession;

import me.bpweber.practiceserver.*;
import me.bpweber.practiceserver.item.*;
import me.bpweber.practiceserver.money.*;
import me.bpweber.practiceserver.vendors.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;
import java.util.concurrent.*;

public class ProfessionMechanics
        implements Listener {
    public void onEnable() {
        PracticeServer.log.info("[ProfessionMechanics] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
    }

    public void onDisable() {
        PracticeServer.log.info("[ProfessionMechanics] has been disabled.");
    }

    public static boolean isSkillItem(final ItemStack is) {
        return ((is.getType() == Material.WOOD_PICKAXE || is.getType() == Material.STONE_PICKAXE || is.getType() == Material.IRON_PICKAXE || is.getType() == Material.DIAMOND_PICKAXE || is.getType() == Material.GOLD_PICKAXE) && is.hasItemMeta() && is.getItemMeta().hasDisplayName()) || (is.getType() == Material.FISHING_ROD && is.hasItemMeta() && is.getItemMeta().hasDisplayName());
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
            if (p.getName().equals("Skill Trainer")) {
                Inventory inv = Bukkit.getServer().createInventory(null, 9, "Skill Trainer");
                ItemStack P = new ItemStack(Material.WOOD_PICKAXE);
                ItemMeta pickmeta = P.getItemMeta();
                pickmeta.setDisplayName(ChatColor.WHITE + "Novice Pickaxe");
                ArrayList<String> lore = new ArrayList<String>();
                lore.add(ChatColor.GRAY + "Level: " + ChatColor.WHITE + "1");
                lore.add(ChatColor.GRAY + "0 / 100");
                lore.add(ChatColor.GRAY + "EXP: " + ChatColor.RED + "||||||||||||||||||||||||||||||||||||||||||||||||||");
                lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "A pickaxe made out of wood.");
                lore.add(ChatColor.GREEN + "Price: " + ChatColor.WHITE + "100g");
                pickmeta.setLore(lore);
                P.setItemMeta(pickmeta);
                inv.addItem(P);
                e.getPlayer().openInventory(inv);
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() != null && e.getInventory().getTitle().equals("Skill Trainer")) {
            List<String> lore;
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.WOOD_PICKAXE && e.getCurrentItem().getItemMeta().hasLore() && (lore = e.getCurrentItem().getItemMeta().getLore()).get(lore.size() - 1).contains("Price:")) {
                int price = ItemVendors.getPriceFromLore(e.getCurrentItem());
                if (Money.hasEnoughGems(p, price)) {
                    ItemStack is = new ItemStack(e.getCurrentItem().getType());
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName());
                    lore.remove(lore.size() - 1);
                    im.setLore(lore);
                    is.setItemMeta(im);
                    ItemVendors.buyingitem.put(p.getName(), is);
                    ItemVendors.buyingprice.put(p.getName(), price);
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

    public static ArrayList<Integer> getExp(ItemStack is) {
        ArrayList<Integer> exp = new ArrayList<Integer>();
        exp.add(0);
        exp.add(0);
        if (is != null && is.getType().name().contains("_PICKAXE") && is.getItemMeta().hasLore() && is.getItemMeta().getLore().size() > 1 && is.getItemMeta().getLore().get(1).contains(" / ")) {
            String line = ChatColor.stripColor(is.getItemMeta().getLore().get(1));
            try {
                exp.set(0, Integer.parseInt(line.split(" / ")[0]));
                exp.set(1, Integer.parseInt(line.split(" / ")[1]));
            } catch (Exception e) {
                return exp;
            }
        }
        return exp;
    }

    public static int getPickaxeLevel(ItemStack is) {
        int level = 0;
        if (is != null && is.getType().name().contains("_PICKAXE") && is.getItemMeta().hasLore() && is.getItemMeta().getLore().size() > 0 && is.getItemMeta().getLore().get(0).contains("Level: ")) {
            String line = ChatColor.stripColor(is.getItemMeta().getLore().get(0));
            try {
                level = Integer.parseInt(line.split("Level: ")[1]);
            } catch (Exception e) {
                return level;
            }
        }
        return level;
    }

    public static int getExpPerLevel(int level) {
        int xp = 100;
        int divide = 5;
        int i = 0;
        while (i < level) {
            if (i < 100) {
                divide = 45;
            }
            if (i < 80) {
                divide = 35;
            }
            if (i < 60) {
                divide = 15;
            }
            if (i < 40) {
                divide = 20;
            }
            if (i < 20) {
                divide = 15;
            }
            xp += xp / divide;
            ++i;
        }
        if (level == 1) {
            xp = 100;
        }
        return xp;
    }

    public static void addExp(Player p, ItemStack is, int xp) {
        int currxp = ProfessionMechanics.getExp(is).get(0);
        int maxxp = ProfessionMechanics.getExp(is).get(1);
        int level = ProfessionMechanics.getPickaxeLevel(is);
        int tier = ProfessionMechanics.getPickaxeTier(is);
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.getLore();
        if (maxxp > 0) {
            p.sendMessage("              " + ChatColor.YELLOW + ChatColor.BOLD + "+" + ChatColor.YELLOW + xp + ChatColor.BOLD + " EXP" + ChatColor.GRAY + " [" + (currxp + xp) + ChatColor.BOLD + "/" + ChatColor.GRAY + maxxp + " EXP]");
            if (currxp + xp > maxxp) {
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                p.sendMessage("          " + ChatColor.YELLOW + ChatColor.BOLD + "PICKAXE LEVEL UP! " + ChatColor.YELLOW + ChatColor.UNDERLINE + (level - 1) + " -> " + ++level);
                int newexp = ProfessionMechanics.getExpPerLevel(level);
                float dura = Durability.getDuraPercent(is) / 100.0f;
                if (level == 20 || level == 40 || level == 60 || level == 80 || level == 100) {
                    if (tier < 5) {
                        ++tier;
                    }
                    if (tier == 2) {
                        is.setType(Material.STONE_PICKAXE);
                        im.setDisplayName(ChatColor.GREEN + "Apprentice Pickaxe");
                        lore.set(lore.size() - 1, ChatColor.GRAY.toString() + ChatColor.ITALIC + "A pickaxe made out of stone.");
                    }
                    if (tier == 3) {
                        is.setType(Material.IRON_PICKAXE);
                        im.setDisplayName(ChatColor.AQUA + "Expert Pickaxe");
                        lore.set(lore.size() - 1, ChatColor.GRAY.toString() + ChatColor.ITALIC + "A pickaxe made out of iron.");
                    }
                    if (tier == 4) {
                        is.setType(Material.DIAMOND_PICKAXE);
                        im.setDisplayName(ChatColor.LIGHT_PURPLE + "Supreme Pickaxe");
                        lore.set(lore.size() - 1, ChatColor.GRAY.toString() + ChatColor.ITALIC + "A pickaxe made out of diamond.");
                    }
                    if (tier == 5) {
                        is.setType(Material.GOLD_PICKAXE);
                        if (level == 80) {
                            im.setDisplayName(ChatColor.YELLOW + "Master Pickaxe");
                        }
                        if (level == 100) {
                            newexp = 0;
                            im.setDisplayName(ChatColor.YELLOW + "Grand Master Pickaxe");
                        }
                        lore.set(lore.size() - 1, ChatColor.GRAY.toString() + ChatColor.ITALIC + "A pickaxe made out of gold.");
                    }
                    short newdura = (short) (is.getType().getMaxDurability() - (short) (dura * (float) is.getType().getMaxDurability()));
                    is.setDurability(newdura);
                }
                lore.set(0, ChatColor.GRAY + "Level: " + ProfessionMechanics.getTierColor(tier) + level);
                lore.set(1, ChatColor.GRAY + "0 / " + newexp);
                lore.set(2, ChatColor.GRAY + "EXP: " + ProfessionMechanics.generateBar(0, newexp));
                im.setLore(lore);
                is.setItemMeta(im);
                if (level == 20 || level == 40 || level == 60 || level == 80 || level == 100) {
                    ProfessionMechanics.upgradePick(p, is);
                }
                p.getInventory().setItemInMainHand(is);
            } else {
                if (maxxp != ProfessionMechanics.getExpPerLevel(level)) {
                    maxxp = ProfessionMechanics.getExpPerLevel(level);
                }
                lore.set(1, ChatColor.GRAY.toString() + (currxp + xp) + " / " + maxxp);
                lore.set(2, ChatColor.GRAY + "EXP: " + ProfessionMechanics.generateBar(currxp + xp, maxxp));
                im.setLore(lore);
                is.setItemMeta(im);
                p.getInventory().setItemInMainHand(is);
            }
        }
    }

    public static String generateBar(int curr, int max) {
        int percent = Math.round(50.0f * ((float) curr / (float) max));
        int barlength = 50;
        String bar = "";
        while (barlength > 0 && percent > 0) {
            --percent;
            --barlength;
            bar = String.valueOf(bar) + "|";
        }
        bar = ChatColor.GREEN + bar;
        bar = String.valueOf(bar) + ChatColor.RED;
        while (barlength > 0) {
            --barlength;
            bar = String.valueOf(bar) + "|";
        }
        if (max == 0) {
            bar = ChatColor.YELLOW.toString();
            int i = 0;
            while (i < 50) {
                bar = String.valueOf(bar) + "|";
                ++i;
            }
        }
        return bar;
    }

    public static void upgradePick(Player p, ItemStack is) {
        Random r = new Random();
        int oreamt = r.nextInt(5) + 1;
        int gemamt = r.nextInt(10) + 1;
        int ench = r.nextInt(3);
        ItemMeta im = is.getItemMeta();
        CopyOnWriteArrayList<String> lore = new CopyOnWriteArrayList<String>(im.getLore());
        String desc = lore.get(lore.size() - 1);
        if (ench == 0) {
            if (ProfessionMechanics.getPickEnchants(is, "GEM FIND") == 10) {
                return;
            }
            if (ProfessionMechanics.getPickEnchants(is, "GEM FIND") >= gemamt && ProfessionMechanics.getPickEnchants(is, "GEM FIND") < 10) {
                gemamt = ProfessionMechanics.getPickEnchants(is, "GEM FIND") + 1;
            }
            for (String s : lore) {
                if (!s.contains("GEM FIND")) continue;
                lore.remove(s);
            }
            lore.set(lore.size() - 1, ChatColor.RED + "GEM FIND: " + gemamt + "%");
            p.sendMessage("          " + ChatColor.YELLOW + ChatColor.BOLD + "PICKAXE UPGRADED: " + ChatColor.RED + "GEM FIND: " + gemamt + "%");
        }
        if (ench == 1) {
            if (ProfessionMechanics.getPickEnchants(is, "DOUBLE ORE") == 5) {
                return;
            }
            if (ProfessionMechanics.getPickEnchants(is, "DOUBLE ORE") >= oreamt && ProfessionMechanics.getPickEnchants(is, "DOUBLE ORE") < 5) {
                oreamt = ProfessionMechanics.getPickEnchants(is, "DOUBLE ORE") + 1;
            }
            for (String s : lore) {
                if (!s.contains("DOUBLE ORE")) continue;
                lore.remove(s);
            }
            lore.set(lore.size() - 1, ChatColor.RED + "DOUBLE ORE: " + oreamt + "%");
            p.sendMessage("          " + ChatColor.YELLOW + ChatColor.BOLD + "PICKAXE UPGRADED: " + ChatColor.RED + "DOUBLE ORE: " + oreamt + "%");
        }
        if (ench == 2) {
            if (ProfessionMechanics.getPickEnchants(is, "TRIPLE ORE") == 5) {
                return;
            }
            if (ProfessionMechanics.getPickEnchants(is, "TRIPLE ORE") >= oreamt && ProfessionMechanics.getPickEnchants(is, "TRIPLE ORE") < 5) {
                oreamt = ProfessionMechanics.getPickEnchants(is, "TRIPLE ORE") + 1;
            }
            for (String s : lore) {
                if (!s.contains("TRIPLE ORE")) continue;
                lore.remove(s);
            }
            lore.set(lore.size() - 1, ChatColor.RED + "TRIPLE ORE: " + oreamt + "%");
            p.sendMessage("          " + ChatColor.YELLOW + ChatColor.BOLD + "PICKAXE UPGRADED: " + ChatColor.RED + "TRIPLE ORE: " + oreamt + "%");
        }
        lore.add(desc);
        im.setLore(lore);
        is.setItemMeta(im);
    }

    public static int getPickEnchants(ItemStack is, String enchant) {
        int ench = 0;
        if (is != null && is.getType().name().contains("_PICKAXE") && is.getItemMeta().hasLore() && is.getItemMeta().getLore().size() > 4) {
            for (String s : is.getItemMeta().getLore()) {
                if (!s.contains(enchant)) continue;
                try {
                    ench = Integer.parseInt(s.split(String.valueOf(enchant) + ": ")[1].split("%")[0]);
                    continue;
                } catch (Exception e) {
                    return ench;
                }
            }
        }
        return ench;
    }

    public static int getPickaxeTier(ItemStack is) {
        if (is.getType() == Material.WOOD_PICKAXE) {
            return 1;
        }
        if (is.getType() == Material.STONE_PICKAXE) {
            return 2;
        }
        if (is.getType() == Material.IRON_PICKAXE) {
            return 3;
        }
        if (is.getType() == Material.DIAMOND_PICKAXE) {
            return 4;
        }
        if (is.getType() == Material.GOLD_PICKAXE) {
            return 5;
        }
        return 0;
    }

    public static int getOreTier(Material m) {
        if (m == Material.COAL_ORE) {
            return 1;
        }
        if (m == Material.EMERALD_ORE) {
            return 2;
        }
        if (m == Material.IRON_ORE) {
            return 3;
        }
        if (m == Material.DIAMOND_ORE) {
            return 4;
        }
        if (m == Material.GOLD_ORE) {
            return 5;
        }
        return 0;
    }

    public static ChatColor getTierColor(int tier) {
        ChatColor cc = ChatColor.WHITE;
        switch (tier) {
            case 1: {
                cc = ChatColor.WHITE;
                break;
            }
            case 2: {
                cc = ChatColor.GREEN;
                break;
            }
            case 3: {
                cc = ChatColor.AQUA;
                break;
            }
            case 4: {
                cc = ChatColor.LIGHT_PURPLE;
                break;
            }
            case 5: {
                cc = ChatColor.YELLOW;
            }
        }
        return cc;
    }

    public static int getFailPercent(int oretier, int level) {
        if (oretier == 1 && level > 19) {
            return 100;
        }
        if (oretier == 2 && level > 39) {
            return 100;
        }
        if (oretier == 3 && level > 59) {
            return 100;
        }
        if (oretier == 4 && level > 79) {
            return 100;
        }
        if (oretier == 5 && level > 99) {
            return 100;
        }
        if (oretier == 1) {
            return 50 + (level - 1) * 2;
        }
        if (oretier == 2) {
            return 50 + (level - 20) * 2;
        }
        if (oretier == 3) {
            return 50 + (level - 40) * 2;
        }
        if (oretier == 4) {
            return 50 + (level - 60) * 2;
        }
        if (oretier == 5) {
            return 50 + (level - 80) * 2;
        }
        return 0;
    }

    public static int getExpFromOre(int tier) {
        if (tier == 1) {
            return 105;
        }
        if (tier == 2) {
            return 245;
        }
        if (tier == 3) {
            return 520;
        }
        if (tier == 4) {
            return 855;
        }
        if (tier == 5) {
            return 1055;
        }
        return 0;
    }
}

