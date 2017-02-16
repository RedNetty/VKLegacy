/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package me.bpweber.practiceserver.player;

import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.party.Parties;
import me.bpweber.practiceserver.pvp.Alignments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Toggles
        implements Listener,
        CommandExecutor {
    public static HashMap<String, ArrayList<String>> toggles = new HashMap<String, ArrayList<String>>();

    public void onEnable() {
        PracticeServer.log.info("[Toggles] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
        File file = new File(PracticeServer.plugin.getDataFolder(), "toggles.yml");
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
        for (String p : config.getKeys(false)) {
            ArrayList<String> toggle = new ArrayList<String>();
            for (String t : config.getStringList(p)) {
                toggle.add(t);
            }
            toggles.put(p, toggle);
        }
    }

    public void onDisable() {
        PracticeServer.log.info("[Toggles] has been disabled.");
        File file = new File(PracticeServer.plugin.getDataFolder(), "toggles.yml");
        YamlConfiguration config = new YamlConfiguration();
        for (String s : toggles.keySet()) {
            config.set(s, toggles.get(s));
        }
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            ArrayList<String> toggle;
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("toggle")) {
                p.openInventory(Toggles.getToggleMenu(p));
            }
            if (cmd.getName().equalsIgnoreCase("toggledebug")) {
                toggle = Toggles.getToggles(p.getName());
                if (toggle.contains("debug")) {
                    toggle.remove("debug");
                    toggles.put(p.getName(), toggle);
                    p.sendMessage((Object) ChatColor.RED + "Debug Messages - " + (Object) ChatColor.BOLD + "DISABLED");
                } else {
                    toggle.add("debug");
                    toggles.put(p.getName(), toggle);
                    p.sendMessage((Object) ChatColor.GREEN + "Debug Messages - " + (Object) ChatColor.BOLD + "ENABLED");
                }
            }
            if (cmd.getName().equalsIgnoreCase("togglepvp")) {
                toggle = Toggles.getToggles(p.getName());
                if (toggle.contains("pvp")) {
                    toggle.remove("pvp");
                    toggles.put(p.getName(), toggle);
                    p.sendMessage((Object) ChatColor.GREEN + "Outgoing PVP Damage - " + (Object) ChatColor.BOLD + "ENABLED");
                } else {
                    toggle.add("pvp");
                    toggles.put(p.getName(), toggle);
                    p.sendMessage((Object) ChatColor.RED + "Outgoing PVP Damage - " + (Object) ChatColor.BOLD + "DISABLED");
                }
            }
            if (cmd.getName().equalsIgnoreCase("togglechaos")) {
                toggle = Toggles.getToggles(p.getName());
                if (toggle.contains("chaos")) {
                    toggle.remove("chaos");
                    toggles.put(p.getName(), toggle);
                    p.sendMessage((Object) ChatColor.RED + "Anti-Chaotic - " + (Object) ChatColor.BOLD + "DISABLED");
                } else {
                    toggle.add("chaos");
                    toggles.put(p.getName(), toggle);
                    p.sendMessage((Object) ChatColor.GREEN + "Anti-Chaotic - " + (Object) ChatColor.BOLD + "ENABLED");
                }
            }
            if (cmd.getName().equalsIgnoreCase("toggleff")) {
                toggle = Toggles.getToggles(p.getName());
                if (toggle.contains("ff")) {
                    toggle.remove("ff");
                    toggles.put(p.getName(), toggle);
                    p.sendMessage((Object) ChatColor.RED + "Friendly Fire - " + (Object) ChatColor.BOLD + "DISABLED");
                } else {
                    toggle.add("ff");
                    toggles.put(p.getName(), toggle);
                    p.sendMessage((Object) ChatColor.GREEN + "Friendly Fire - " + (Object) ChatColor.BOLD + "ENABLED");
                }
            }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNoDamageToggle(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            Player pp = (Player) e.getEntity();
            if (e.getDamage() <= 0.0) {
                return;
            }
            ArrayList<String> gettoggles = Toggles.getToggles(p.getName());
            ArrayList<String> buddies = Buddies.getBuddies(p.getName());
            if (buddies.contains(pp.getName().toLowerCase()) && !gettoggles.contains("ff")) {
                e.setDamage(0.0);
                e.setCancelled(true);
                return;
            }
            if (Parties.arePartyMembers(p, pp)) {
                e.setDamage(0.0);
                e.setCancelled(true);
                return;
            }
            if (gettoggles.contains("pvp")) {
                e.setDamage(0.0);
                e.setCancelled(true);
                return;
            }
            if (!Alignments.neutral.containsKey(pp.getName()) && !Alignments.chaotic.containsKey(pp.getName()) && gettoggles.contains("chaos")) {
                e.setDamage(0.0);
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onClickToggle(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (p.getOpenInventory().getTopInventory().getName().equals("Toggle Menu")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && e.getCurrentItem().getItemMeta().hasDisplayName() && e.getCurrentItem().getItemMeta().getDisplayName().contains("/toggle")) {
                String name = ChatColor.stripColor((String) e.getCurrentItem().getItemMeta().getDisplayName());
                name = name.substring(1, name.length());
                p.performCommand(name);
                boolean on = e.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.RED.toString());
                e.setCurrentItem(Toggles.getToggleButton(name, on));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.5f);
            }
        }
    }

    public static ArrayList<String> getToggles(String s) {
        if (toggles.containsKey(s)) {
            return toggles.get(s);
        }
        return new ArrayList<String>();
    }

    public static Inventory getToggleMenu(Player p) {
        ArrayList<String> toggles = Toggles.getToggles(p.getName());
        Inventory inv = Bukkit.getServer().createInventory(null, 9, "Toggle Menu");
        if (toggles.contains("pvp")) {
            inv.setItem(0, Toggles.getToggleButton("togglepvp", true));
        } else {
            inv.setItem(0, Toggles.getToggleButton("togglepvp", false));
        }
        if (toggles.contains("chaos")) {
            inv.setItem(1, Toggles.getToggleButton("togglechaos", true));
        } else {
            inv.setItem(1, Toggles.getToggleButton("togglechaos", false));
        }
        if (toggles.contains("ff")) {
            inv.setItem(2, Toggles.getToggleButton("toggleff", true));
        } else {
            inv.setItem(2, Toggles.getToggleButton("toggleff", false));
        }
        if (toggles.contains("debug")) {
            inv.setItem(3, Toggles.getToggleButton("toggledebug", true));
        } else {
            inv.setItem(3, Toggles.getToggleButton("toggledebug", false));
        }
        return inv;
    }

    public static ItemStack getToggleButton(String s, boolean on) {
        ItemStack is = new ItemStack(Material.INK_SACK);
        ItemMeta im = is.getItemMeta();
        ChatColor cc = null;
        if (on) {
            is.setDurability((short) 10);
            cc = ChatColor.GREEN;
        } else {
            is.setDurability((short) 8);
            cc = ChatColor.RED;
        }
        im.setDisplayName((Object) cc + "/" + s);
        im.setLore(Arrays.asList(Toggles.getToggleDescription(s)));
        is.setItemMeta(im);
        return is;
    }

    public static String getToggleDescription(String toggle) {
        String desc = ChatColor.GRAY.toString();
        if (toggle.equalsIgnoreCase("toggledebug")) {
            desc = String.valueOf(desc) + "Toggles displaying combat debug messages.";
        }
        if (toggle.equalsIgnoreCase("toggleff")) {
            desc = String.valueOf(desc) + "Toggles friendly-fire between buddies.";
        }
        if (toggle.equalsIgnoreCase("togglechaos")) {
            desc = String.valueOf(desc) + "Toggles killing blows on lawful players (anti-chaotic).";
        }
        if (toggle.equalsIgnoreCase("togglepvp")) {
            desc = String.valueOf(desc) + "Toggles all outgoing PvP damage (anti-neutral).";
        }
        return desc;
    }
}

