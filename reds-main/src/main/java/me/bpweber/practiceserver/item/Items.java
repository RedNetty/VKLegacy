/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.bpweber.practiceserver.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Items {
    public static ItemStack orb(boolean inshop) {
        ItemStack orb = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta orbmeta = orb.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        orbmeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Orb of Alteration");
        lore.add(ChatColor.GRAY + "Randomizes stats of selected equipment.");
        if (inshop) {
            lore.add(ChatColor.GREEN + "Price: " + ChatColor.WHITE + "2000g");
        }
        orbmeta.setLore(lore);
        orb.setItemMeta(orbmeta);
        return orb;
    }

    public static ItemStack signNewCustomItem(final Material m, final String name, final String desc) {
        final ItemStack is = new ItemStack(m);
        final ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        final List<String> new_lore = new ArrayList<String>();
        if (desc.contains(",")) {
            String[] split;
            for (int length = (split = desc.split(",")).length, i = 0; i < length; ++i) {
                final String s = split[i];
                new_lore.add(s);
            }
        } else {
            new_lore.add(desc);
        }
        im.setLore(new_lore);
        is.setItemMeta(im);
        return is;
    }
    public static ItemStack enchant(int tier, int type, boolean inshop) {
        ItemStack is = new ItemStack(Material.EMPTY_MAP);
        ItemMeta im = is.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        String name = "";
        int price = 0;
        if (tier == 1) {
            price = 100;
            name = ChatColor.WHITE + " Enchant ";
            if (type == 0) {
                name = String.valueOf(name) + "Wooden";
            }
            if (type == 1) {
                name = String.valueOf(name) + "Leather";
            }
        }
        if (tier == 2) {
            price = 200;
            name = ChatColor.GREEN + " Enchant ";
            if (type == 0) {
                name = String.valueOf(name) + "Stone";
            }
            if (type == 1) {
                name = String.valueOf(name) + "Chainmail";
            }
        }
        if (tier == 3) {
            price = 400;
            name = ChatColor.AQUA + " Enchant Iron";
        }
        if (tier == 4) {
            price = 800;
            name = ChatColor.LIGHT_PURPLE + " Enchant Diamond";
        }
        if (tier == 5) {
            price = 1600;
            name = ChatColor.YELLOW + " Enchant Gold";
        }
        if (type == 0) {
            price = (int) ((double) price * 1.5);
            name = String.valueOf(name) + " Weapon";
            lore.add(ChatColor.RED + "+5% DMG");
            lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Weapon will VANISH if enchant above +3 FAILS.");
        }
        if (type == 1) {
            name = String.valueOf(name) + " Armor";
            lore.add(ChatColor.RED + "+5% HP");
            lore.add(ChatColor.RED + "+5% HP REGEN");
            lore.add(ChatColor.GRAY + "   - OR -");
            lore.add(ChatColor.RED + "+1% ENERGY REGEN");
            lore.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Armor will VANISH if enchant above +3 FAILS.");
        }
        if (inshop) {
            lore.add(ChatColor.GREEN + "Price: " + ChatColor.WHITE + price + "g");
        }
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Scroll:" + name);
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }
}

