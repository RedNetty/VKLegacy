/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package me.bpweber.practiceserver.drops;

import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

public class CreateDrop {
    public static ItemStack createDrop(int tier, int item, int raree) {
        int min_min_dmg;
        int max_min_dmg;
        int min_max_dmg;
        int max_max_dmg;
        String name = "";
        String rare = "";
        ItemStack is = new ItemStack(Material.AIR);
        ArrayList<String> lore = new ArrayList<String>();
        Random random = new Random();
        int rarity = 0;
        int armdps = random.nextInt(2) + 1;
        int nrghp = random.nextInt(2) + 1;
        int elem = random.nextInt(12) + 1;
        int pure = random.nextInt(4) + 1;
        int life = random.nextInt(4) + 1;
        int crit = random.nextInt(4) + 1;
        int acc = random.nextInt(4) + 1;
        int dodge = random.nextInt(3) + 1;
        int block = random.nextInt(3) + 1;
        int vit = random.nextInt(3) + 1;
        int str = random.nextInt(3) + 1;
        int intel = random.nextInt(3) + 1;
        int hp = 0;
        int mindmg = 0;
        int maxdmg = 0;
        int dpsamt = 0;
        int dodgeamt = 0;
        int blockamt = 0;
        int vitamt = 0;
        int intamt = 0;
        int stramt = 0;
        int elemamt = 0;
        int pureamt = 0;
        int lifeamt = 0;
        int critamt = 0;
        int accamt = 0;
        int hps = 0;
        int nrg = 0;
        if (raree == 3) {
            rarity = 3;
            rare = ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Unique";
        } else if (raree == 2) {
            rarity = 2;
            rare = ChatColor.AQUA.toString() + ChatColor.ITALIC + "Rare";
        } else if (raree == 1) {
            rarity = 1;
            rare = ChatColor.GREEN.toString() + ChatColor.ITALIC + "Uncommon";
        } else if (raree == 0) {
            rarity = 0;
            rare = ChatColor.GRAY.toString() + ChatColor.ITALIC + "Common";
        }
        if (tier == 1) {
            tier = 1;
            if (rarity == 0) {
                dpsamt = 1;
                hp = random.nextInt(21) + 10;
                min_min_dmg = 1;
                max_min_dmg = 3;
                max_max_dmg = 6;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            if (rarity == 1) {
                dpsamt = random.nextInt(3) + 1;
                hp = random.nextInt(30) + 31;
                min_min_dmg = 3;
                max_min_dmg = 6;
                max_max_dmg = 9;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            if (rarity == 2) {
                dpsamt = random.nextInt(3) + 1;
                hp = random.nextInt(30) + 61;
                min_min_dmg = 6;
                max_min_dmg = 10;
                max_max_dmg = 24;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            if (rarity == 3) {
                dpsamt = random.nextInt(3) + 1;
                hp = random.nextInt(30) + 91;
                min_min_dmg = 9;
                max_min_dmg = 16;
                max_max_dmg = 25;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            hps = random.nextInt(11) + 5;
            nrg = random.nextInt(3) + 1;
            dodgeamt = random.nextInt(5) + 1;
            blockamt = random.nextInt(5) + 1;
            vitamt = random.nextInt(15) + 1;
            stramt = random.nextInt(15) + 1;
            intamt = random.nextInt(15) + 1;
            elemamt = random.nextInt(4) + 1;
            pureamt = random.nextInt(4) + 1;
            lifeamt = random.nextInt(30) + 1;
            critamt = random.nextInt(3) + 1;
            accamt = random.nextInt(10) + 1;
            if (item == 0) {
                name = "Staff";
                is.setType(Material.WOOD_HOE);
            }
            if (item == 1) {
                name = "Spear";
                is.setType(Material.WOOD_SPADE);
            }
            if (item == 2) {
                name = "Shortsword";
                is.setType(Material.WOOD_SWORD);
            }
            if (item == 3) {
                name = "Hatchet";
                is.setType(Material.WOOD_AXE);
            }
            if (item == 4) {
                name = "Leather Coif";
                is.setType(Material.LEATHER_HELMET);
            }
            if (item == 5) {
                name = "Leather Chestplate";
                is.setType(Material.LEATHER_CHESTPLATE);
            }
            if (item == 6) {
                name = "Leather Leggings";
                is.setType(Material.LEATHER_LEGGINGS);
            }
            if (item == 7) {
                name = "Leather Boots";
                is.setType(Material.LEATHER_BOOTS);
            }
        }
        if (tier == 2) {
            tier = 2;
            if (rarity == 0) {
                dpsamt = random.nextInt(3) + 1;
                hp = random.nextInt(41) + 70;
                min_min_dmg = 10;
                max_min_dmg = 13;
                max_max_dmg = 18;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            if (rarity == 1) {
                dpsamt = random.nextInt(3) + 3;
                hp = random.nextInt(80) + 111;
                min_min_dmg = 16;
                max_min_dmg = 19;
                max_max_dmg = 25;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            if (rarity == 2) {
                dpsamt = random.nextInt(3) + 5;
                hp = random.nextInt(50) + 191;
                min_min_dmg = 20;
                max_min_dmg = 31;
                max_max_dmg = 66;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            if (rarity == 3) {
                dpsamt = random.nextInt(3) + 5;
                hp = random.nextInt(70) + 241;
                min_min_dmg = 22;
                max_min_dmg = 36;
                max_max_dmg = 71;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            hps = random.nextInt(16) + 10;
            nrg = random.nextInt(3) + 1;
            dodgeamt = random.nextInt(8) + 1;
            blockamt = random.nextInt(8) + 1;
            vitamt = random.nextInt(35) + 1;
            stramt = random.nextInt(35) + 1;
            intamt = random.nextInt(35) + 1;
            elemamt = random.nextInt(9) + 1;
            pureamt = random.nextInt(9) + 1;
            lifeamt = random.nextInt(15) + 1;
            critamt = random.nextInt(6) + 1;
            accamt = random.nextInt(12) + 1;
            if (item == 0) {
                name = "Battletaff";
                is.setType(Material.STONE_HOE);
            }
            if (item == 1) {
                name = "Halberd";
                is.setType(Material.STONE_SPADE);
            }
            if (item == 2) {
                name = "Broadsword";
                is.setType(Material.STONE_SWORD);
            }
            if (item == 3) {
                name = "Great Axe";
                is.setType(Material.STONE_AXE);
            }
            if (item == 4) {
                name = "Medium Helmet";
                is.setType(Material.CHAINMAIL_HELMET);
            }
            if (item == 5) {
                name = "Chainmail";
                is.setType(Material.CHAINMAIL_CHESTPLATE);
            }
            if (item == 6) {
                name = "Chainmail Leggings";
                is.setType(Material.CHAINMAIL_LEGGINGS);
            }
            if (item == 7) {
                name = "Chainmail Boots";
                is.setType(Material.CHAINMAIL_BOOTS);
            }
        }
        if (tier == 3) {
            tier = 3;
            if (rarity == 0) {
                dpsamt = random.nextInt(3) + 5;
                hp = random.nextInt(251) + 200;
                min_min_dmg = 25;
                max_min_dmg = 31;
                max_max_dmg = 46;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            if (rarity == 1) {
                dpsamt = random.nextInt(5) + 6;
                hp = random.nextInt(200) + 451;
                min_min_dmg = 30;
                max_min_dmg = 36;
                max_max_dmg = 71;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            if (rarity == 2) {
                dpsamt = random.nextInt(3) + 8;
                hp = random.nextInt(99) + 651;
                min_min_dmg = 50;
                max_min_dmg = 91;
                max_max_dmg = 151;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            if (rarity == 3) {
                dpsamt = random.nextInt(4) + 8;
                hp = random.nextInt(101) + 750;
                min_min_dmg = 60;
                max_min_dmg = 101;
                max_max_dmg = 161;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg - mindmg) + mindmg;
            }
            hps = random.nextInt(21) + 35;
            nrg = random.nextInt(3) + 2;
            dodgeamt = random.nextInt(10) + 1;
            blockamt = random.nextInt(10) + 1;
            vitamt = random.nextInt(75) + 1;
            stramt = random.nextInt(75) + 1;
            intamt = random.nextInt(75) + 1;
            elemamt = random.nextInt(15) + 1;
            pureamt = random.nextInt(15) + 1;
            lifeamt = random.nextInt(12) + 1;
            critamt = random.nextInt(8) + 1;
            accamt = random.nextInt(25) + 1;
            if (item == 0) {
                name = "Wizard Staff";
                is.setType(Material.IRON_HOE);
            }
            if (item == 1) {
                name = "Magic Polearm";
                is.setType(Material.IRON_SPADE);
            }
            if (item == 2) {
                name = "Magic Sword";
                is.setType(Material.IRON_SWORD);
            }
            if (item == 3) {
                name = "War Axe";
                is.setType(Material.IRON_AXE);
            }
            if (item == 4) {
                name = "Full Helmet";
                is.setType(Material.IRON_HELMET);
            }
            if (item == 5) {
                name = "Platemail";
                is.setType(Material.IRON_CHESTPLATE);
            }
            if (item == 6) {
                name = "Platemail Leggings";
                is.setType(Material.IRON_LEGGINGS);
            }
            if (item == 7) {
                name = "Platemail Boots";
                is.setType(Material.IRON_BOOTS);
            }
        }
        if (tier == 4) {
            tier = 4;
            if (rarity == 0) {
                dpsamt = random.nextInt(3) + 8;
                hp = random.nextInt(311) + 650;
                min_min_dmg = 65;
                max_min_dmg = 81;
                max_max_dmg = 30;
                min_max_dmg = 90;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg) + min_max_dmg;
            }
            if (rarity == 1) {
                dpsamt = random.nextInt(3) + 10;
                hp = random.nextInt(490) + 961;
                min_min_dmg = 70;
                max_min_dmg = 86;
                max_max_dmg = 35;
                min_max_dmg = 120;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg) + min_max_dmg;
            }
            if (rarity == 2) {
                dpsamt = random.nextInt(3) + 11;
                hp = random.nextInt(850) + 1451;
                min_min_dmg = 90;
                max_min_dmg = 100;
                max_max_dmg = 50;
                min_max_dmg = 160;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg) + min_max_dmg;
            }
            if (rarity == 3) {
                dpsamt = random.nextInt(3) + 12;
                hp = random.nextInt(500) + 2301;
                min_min_dmg = 90;
                max_min_dmg = 100;
                max_max_dmg = 60;
                min_max_dmg = 199;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg) + min_max_dmg;
            }
            hps = random.nextInt(16) + 60;
            nrg = random.nextInt(3) + 3;
            dodgeamt = random.nextInt(12) + 1;
            blockamt = random.nextInt(12) + 1;
            vitamt = random.nextInt(115) + 1;
            stramt = random.nextInt(115) + 1;
            intamt = random.nextInt(115) + 1;
            elemamt = random.nextInt(25) + 1;
            pureamt = random.nextInt(25) + 1;
            lifeamt = random.nextInt(10) + 1;
            critamt = random.nextInt(10) + 1;
            accamt = random.nextInt(28) + 1;
            if (item == 0) {
                name = "Ancient Staff";
                is.setType(Material.DIAMOND_HOE);
            }
            if (item == 1) {
                name = "Ancient Polearm";
                is.setType(Material.DIAMOND_SPADE);
            }
            if (item == 2) {
                name = "Ancient Sword";
                is.setType(Material.DIAMOND_SWORD);
            }
            if (item == 3) {
                name = "Ancient Axe";
                is.setType(Material.DIAMOND_AXE);
            }
            if (item == 4) {
                name = "Ancient Full Helmet";
                is.setType(Material.DIAMOND_HELMET);
            }
            if (item == 5) {
                name = "Magic Platemail";
                is.setType(Material.DIAMOND_CHESTPLATE);
            }
            if (item == 6) {
                name = "Magic Platemail Leggings";
                is.setType(Material.DIAMOND_LEGGINGS);
            }
            if (item == 7) {
                name = "Magic Platemail Boots";
                is.setType(Material.DIAMOND_BOOTS);
            }
        }
        if (tier == 5) {
            tier = 5;
            if (rarity == 0) {
                dpsamt = random.nextInt(3) + 11;
                hp = random.nextInt(1051) + 1450;
                min_min_dmg = 130;
                max_min_dmg = 141;
                max_max_dmg = 56;
                min_max_dmg = 157;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg) + min_max_dmg;
            }
            if (rarity == 1) {
                dpsamt = random.nextInt(3) + 13;
                hp = random.nextInt(1300) + 2501;
                min_min_dmg = 150;
                max_min_dmg = 161;
                max_max_dmg = 91;
                min_max_dmg = 210;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg) + min_max_dmg;
            }
            if (rarity == 2) {
                dpsamt = random.nextInt(3) + 16;
                hp = random.nextInt(1700) + 3801;
                min_min_dmg = 190;
                max_min_dmg = 231;
                max_max_dmg = 120;
                min_max_dmg = 321;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg) + min_max_dmg;

            }
            if (rarity == 3) {
                dpsamt = random.nextInt(3) + 17;
                hp = random.nextInt(500) + 5501;
                min_min_dmg = 190;
                max_min_dmg = 251;
                max_max_dmg = 150;
                min_max_dmg = 350;
                mindmg = random.nextInt(max_min_dmg - min_min_dmg) + min_min_dmg;
                maxdmg = random.nextInt(max_max_dmg) + min_max_dmg;
            }
            hps = random.nextInt(41) + 80;
            nrg = random.nextInt(3) + 3;
            dodgeamt = random.nextInt(12) + 1;
            blockamt = random.nextInt(12) + 1;
            vitamt = random.nextInt(315) + 1;
            stramt = random.nextInt(315) + 1;
            intamt = random.nextInt(315) + 1;
            elemamt = random.nextInt(55) + 1;
            pureamt = random.nextInt(55) + 1;
            lifeamt = random.nextInt(8) + 1;
            critamt = random.nextInt(11) + 1;
            accamt = random.nextInt(35) + 1;
            if (item == 0) {
                name = "Legendary Staff";
                is.setType(Material.GOLD_HOE);
            }
            if (item == 1) {
                name = "Legendary Polearm";
                is.setType(Material.GOLD_SPADE);
            }
            if (item == 2) {
                name = "Legendary Sword";
                is.setType(Material.GOLD_SWORD);
            }
            if (item == 3) {
                name = "Legendary Axe";
                is.setType(Material.GOLD_AXE);
            }
            if (item == 4) {
                name = "Legendary Full Helmet";
                is.setType(Material.GOLD_HELMET);
            }
            if (item == 5) {
                name = "Legendary Platemail";
                is.setType(Material.GOLD_CHESTPLATE);
            }
            if (item == 6) {
                name = "Legendary Platemail Leggings";
                is.setType(Material.GOLD_LEGGINGS);
            }
            if (item == 7) {
                name = "Legendary Platemail Boots";
                is.setType(Material.GOLD_BOOTS);
            }
        }
        if (item == 0 || item == 1) {
            if ((mindmg = (int) ((double) mindmg * 0.5)) < 1) {
                mindmg = 1;
            }
            if ((maxdmg = (int) ((double) maxdmg * 0.5)) < 1) {
                maxdmg = 1;
            }
            lore.add(ChatColor.RED + "DMG: " + mindmg + " - " + maxdmg);
            if (life == 1) {
                lore.add(ChatColor.RED + "LIFE STEAL: " + lifeamt + "%");
                name = "Vampyric " + name;
            }
            if (crit == 1) {
                lore.add(ChatColor.RED + "CRITICAL HIT: " + critamt + "%");
                name = "Deadly " + name;
            }
            if (elem == 3) {
                lore.add(ChatColor.RED + "ICE DMG: +" + elemamt);
                name = String.valueOf(name) + " of Ice";
            }
            if (elem == 2) {
                lore.add(ChatColor.RED + "POISON DMG: +" + elemamt);
                name = String.valueOf(name) + " of Poison";
            }
            if (elem == 1) {
                lore.add(ChatColor.RED + "FIRE DMG: +" + elemamt);
                name = String.valueOf(name) + " of Fire";
            }
        }
        if (item == 2) {
            lore.add(ChatColor.RED + "DMG: " + mindmg + " - " + maxdmg);
            if (acc == 1) {
                lore.add(ChatColor.RED + "ACCURACY: " + accamt + "%");
                name = "Accurate " + name;
            }
            if (life == 1) {
                lore.add(ChatColor.RED + "LIFE STEAL: " + lifeamt + "%");
                name = "Vampyric " + name;
            }
            if (crit == 1) {
                lore.add(ChatColor.RED + "CRITICAL HIT: " + critamt + "%");
                name = "Deadly " + name;
            }
            if (elem == 3) {
                lore.add(ChatColor.RED + "ICE DMG: +" + elemamt);
                name = String.valueOf(name) + " of Ice";
            }
            if (elem == 2) {
                lore.add(ChatColor.RED + "POISON DMG: +" + elemamt);
                name = String.valueOf(name) + " of Poison";
            }
            if (elem == 1) {
                lore.add(ChatColor.RED + "FIRE DMG: +" + elemamt);
                name = String.valueOf(name) + " of Fire";
            }
        }
        if (item == 3) {
            lore.add(ChatColor.RED + "DMG: " + (int) ((double) mindmg) + " - " + (int) ((double) maxdmg));
            if (pure == 1) {
                lore.add(ChatColor.RED + "PURE DMG: +" + pureamt);
                name = "Pure " + name;
            }
            if (life == 1) {
                lore.add(ChatColor.RED + "LIFE STEAL: " + lifeamt + "%");
                name = "Vampyric " + name;
            }
            if (crit == 1) {
                lore.add(ChatColor.RED + "CRITICAL HIT: " + critamt + "%");
                name = "Deadly " + name;
            }
            if (elem == 3) {
                lore.add(ChatColor.RED + "ICE DMG: +" + elemamt);
                name = String.valueOf(name) + " of Ice";
            }
            if (elem == 2) {
                lore.add(ChatColor.RED + "POISON DMG: +" + elemamt);
                name = String.valueOf(name) + " of Poison";
            }
            if (elem == 1) {
                lore.add(ChatColor.RED + "FIRE DMG: +" + elemamt);
                name = String.valueOf(name) + " of Fire";
            }
        }
        if (item == 4 || item == 7) {
            if ((dpsamt = (int) ((double) dpsamt * 0.5)) < 1) {
                dpsamt = 1;
            }
            if ((hp = (int) ((double) hp * 0.5)) < 1) {
                hp = 1;
            }
            if (armdps == 1) {
                lore.add(ChatColor.RED + "ARMOR: " + dpsamt + " - " + dpsamt + "%");
            }
            if (armdps == 2) {
                lore.add(ChatColor.RED + "DPS: " + dpsamt + " - " + dpsamt + "%");
            }
            lore.add(ChatColor.RED + "HP: +" + hp);
            if (nrghp == 2) {
                lore.add(ChatColor.RED + "ENERGY REGEN: +" + nrg + "%");
            }
            if (nrghp == 1) {
                lore.add(ChatColor.RED + "HP REGEN: +" + hps + " HP/s");
            }
            if (intel == 1) {
                lore.add(ChatColor.RED + "INT: +" + intamt);
            }
            if (str == 1) {
                lore.add(ChatColor.RED + "STR: +" + stramt);
            }
            if (vit == 1) {
                lore.add(ChatColor.RED + "VIT: +" + vitamt);
            }
            if (dodge == 1) {
                lore.add(ChatColor.RED + "DODGE: " + dodgeamt + "%");
                name = "Agile " + name;
            }
            if (nrghp == 1) {
                name = "Mending " + name;
            }
            if (block == 1) {
                lore.add(ChatColor.RED + "BLOCK: " + blockamt + "%");
                name = "Protective " + name;
            }
            if (nrghp == 2) {
                name = String.valueOf(name) + " of Fortitude";
            }
        }
        if (item == 5 || item == 6) {
            if (armdps == 1) {
                lore.add(ChatColor.RED + "ARMOR: " + dpsamt + " - " + dpsamt + "%");
            }
            if (armdps == 2) {
                lore.add(ChatColor.RED + "DPS: " + dpsamt + " - " + dpsamt + "%");
            }
            lore.add(ChatColor.RED + "HP: +" + hp);
            if (nrghp == 2) {
                lore.add(ChatColor.RED + "ENERGY REGEN: +" + nrg + "%");
            }
            if (nrghp == 1) {
                lore.add(ChatColor.RED + "HP REGEN: +" + hps + " HP/s");
            }
            if (intel == 1) {
                lore.add(ChatColor.RED + "INT: +" + intamt);
            }
            if (str == 1) {
                lore.add(ChatColor.RED + "STR: +" + stramt);
            }
            if (vit == 1) {
                lore.add(ChatColor.RED + "VIT: +" + vitamt);
            }
            if (dodge == 1) {
                lore.add(ChatColor.RED + "DODGE: " + dodgeamt + "%");
                name = "Agile " + name;
            }
            if (nrghp == 1) {
                name = "Mending " + name;
            }
            if (block == 1) {
                lore.add(ChatColor.RED + "BLOCK: " + blockamt + "%");
                name = "Protective " + name;
            }
            if (nrghp == 2) {
                name = String.valueOf(name) + " of Fortitude";
            }
        }
        lore.add(rare);
        if (tier == 1) {
            name = ChatColor.WHITE + name;
        }
        if (tier == 2) {
            name = ChatColor.GREEN + name;
        }
        if (tier == 3) {
            name = ChatColor.AQUA + name;
        }
        if (tier == 4) {
            name = ChatColor.LIGHT_PURPLE + name;
        }
        if (tier == 5) {
            name = ChatColor.YELLOW + name;
        }
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        for (ItemFlag itemFlag : ItemFlag.values()) {
            im.addItemFlags(itemFlag);
        }
        is.setItemMeta(im);
        return is;
    }
}

