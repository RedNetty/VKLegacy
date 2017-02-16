package me.bpweber.practiceserver.loot;

import me.bpweber.practiceserver.PracticeServer;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LootChests
        implements Listener,
        CommandExecutor {
    static HashMap<Location, Integer> loot = new HashMap<Location, Integer>();
    static HashMap<Location, Integer> respawn = new HashMap<Location, Integer>();
    static HashMap<String, Location> creatingloot = new HashMap<String, Location>();
    HashMap<Location, Inventory> opened = new HashMap<Location, Inventory>();
    HashMap<Player, Location> viewers = new HashMap<Player, Location>();

    public void onEnable() {
        PracticeServer.log.info("[LootChests] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
        Bukkit.getServer().getPluginManager().registerEvents((Listener) this, PracticeServer.plugin);
        new BukkitRunnable() {

            public void run() {
                for (Location loc : LootChests.loot.keySet()) {
                    if (LootChests.respawn.containsKey(loc)) {
                        if (LootChests.respawn.get(loc) >= 1) {
                            LootChests.respawn.put(loc, LootChests.respawn.get(loc) - 1);
                            continue;
                        }
                        LootChests.respawn.remove(loc);
                        continue;
                    }
                    if (!loc.getWorld().getChunkAt(loc).isLoaded() || loc.getWorld().getBlockAt(loc).getType().equals(Material.GLOWSTONE))
                        continue;
                    loc.getWorld().getBlockAt(loc).setType(Material.CHEST);
                }
            }
        }.runTaskTimer(PracticeServer.plugin, 20, 20);
        File file = new File(PracticeServer.plugin.getDataFolder(), "loot.yml");
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
        for (String key : config.getKeys(false)) {
            int val = config.getInt(key);
            String[] str = key.split(",");
            World world = Bukkit.getWorld((String) str[0]);
            double x = Double.valueOf(str[1]);
            double y = Double.valueOf(str[2]);
            double z = Double.valueOf(str[3]);
            Location loc = new Location(world, x, y, z);
            loot.put(loc, val);
        }
    }

    public void onDisable() {
        PracticeServer.log.info("[LootChests] has been disabled.");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p;
        if (sender instanceof Player && (p = (Player) sender).isOp()) {
            World w;
            int z;
            int k;
            int count;
            int y;
            Location loc;
            int radius;
            int x;
            int j;
            int i;
            if (cmd.getName().equalsIgnoreCase("showloot")) {
                if (args.length != 1) {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Incorrect Syntax. " + ChatColor.RED + "/showloot <radius>");
                    return true;
                }
                radius = 0;
                try {
                    radius = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    radius = 0;
                }
                loc = p.getLocation();
                w = loc.getWorld();
                x = (int) loc.getX();
                y = (int) loc.getY();
                z = (int) loc.getZ();
                count = 0;
                i = -radius;
                while (i <= radius) {
                    j = -radius;
                    while (j <= radius) {
                        k = -radius;
                        while (k <= radius) {
                            loc = w.getBlockAt(x + i, y + j, z + k).getLocation();
                            if (loot.containsKey(loc)) {
                                ++count;
                                loc.getBlock().setType(Material.GLOWSTONE);
                            }
                            ++k;
                        }
                        ++j;
                    }
                    ++i;
                }
                p.sendMessage(ChatColor.YELLOW + "Displaying " + count + " lootchests in a " + radius + " block radius...");
                p.sendMessage(ChatColor.GRAY + "Break them to unregister the spawn point.");
            }
            if (cmd.getName().equalsIgnoreCase("hideloot")) {
                if (args.length != 1) {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Incorrect Syntax. " + ChatColor.RED + "/hideloot <radius>");
                    return true;
                }
                radius = 0;
                try {
                    radius = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    radius = 0;
                }
                loc = p.getLocation();
                w = loc.getWorld();
                x = (int) loc.getX();
                y = (int) loc.getY();
                z = (int) loc.getZ();
                count = 0;
                i = -radius;
                while (i <= radius) {
                    j = -radius;
                    while (j <= radius) {
                        k = -radius;
                        while (k <= radius) {
                            loc = w.getBlockAt(x + i, y + j, z + k).getLocation();
                            if (loot.containsKey(loc) && loc.getBlock().getType() == Material.GLOWSTONE) {
                                loc.getBlock().setType(Material.AIR);
                                ++count;
                            }
                            ++k;
                        }
                        ++j;
                    }
                    ++i;
                }
                p.sendMessage(ChatColor.YELLOW + "Hiding " + count + " loot chests in a " + radius + " block radius...");
            }
        }
        return false;
    }

    public boolean isMobNear(Player p) {
        for (Entity ent : p.getNearbyEntities(6.0, 6.0, 6.0)) {
            if (!(ent instanceof LivingEntity) || ent instanceof Player || ent instanceof Horse) continue;
            return true;
        }
        return false;
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player p = e.getPlayer();
            if (e.hasBlock()) {
                if (e.getClickedBlock().getType() == Material.CHEST) {
                    Location loc = e.getClickedBlock().getLocation();
                    if (loot.containsKey(loc)) {
                        e.setCancelled(true);
                        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            if (this.isMobNear(p)) {
                                p.sendMessage(ChatColor.RED + "It is " + ChatColor.BOLD + "NOT" + ChatColor.RED + " safe to open that right now.");
                                p.sendMessage(ChatColor.GRAY + "Eliminate the monsters in the area first.");
                            } else if (!this.opened.containsKey(loc)) {
                                Inventory inv = Bukkit.createInventory((InventoryHolder) null, (int) 27, (String) "Loot Chest");
                                inv.addItem(new ItemStack[]{LootDrops.createLootDrop(loot.get(loc))});
                                p.openInventory(inv);
                                p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
                                this.viewers.put(e.getPlayer(), loc);
                                this.opened.put(loc, inv);
                            } else {
                                p.openInventory(this.opened.get(loc));
                                p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
                                this.viewers.put(e.getPlayer(), loc);
                            }
                        } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                            if (this.isMobNear(p)) {
                                p.sendMessage(ChatColor.RED + "It is " + ChatColor.BOLD + "NOT" + ChatColor.RED + " safe to open that right now.");
                                p.sendMessage(ChatColor.GRAY + "Eliminate the monsters in the area first.");
                            } else if (this.opened.containsKey(loc)) {
                                loc.getWorld().getBlockAt(loc).setType(Material.AIR);
                                p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 0.5f, 1.2f);
                                ItemStack[] arritemStack = this.opened.get(loc).getContents();
                                int n = arritemStack.length;
                                int n2 = 0;
                                while (n2 < n) {
                                    ItemStack is = arritemStack[n2];
                                    if (is != null) {
                                        loc.getWorld().dropItemNaturally(loc, is);
                                    }
                                    ++n2;
                                }
                                this.opened.remove(loc);
                                int tier = loot.get(loc);
                                respawn.put(loc, 60 * tier);
                                for (Player v : this.viewers.keySet()) {
                                    if (!this.viewers.get(v).equals(loc)) continue;
                                    this.viewers.remove(v);
                                    v.closeInventory();
                                    v.playSound(v.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 0.5f, 1.2f);
                                    v.playSound(v.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1.0f, 1.0f);
                                }
                            } else {
                                loc.getWorld().getBlockAt(loc).setType(Material.AIR);
                                loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.WOOD);
                                p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 0.5f, 1.2f);
                                loc.getWorld().dropItemNaturally(loc, LootDrops.createLootDrop(loot.get(loc)));
                                int tier = loot.get(loc);
                                respawn.put(loc, 60 * tier);
                                for (Player v : this.viewers.keySet()) {
                                    if (!this.viewers.get(v).equals(loc)) continue;
                                    this.viewers.remove(v);
                                    v.closeInventory();
                                    v.playSound(v.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 0.5f, 1.2f);
                                    v.playSound(v.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1.0f, 1.0f);
                                }
                            }
                        }
                    } else if (!p.isOp()) {
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.GRAY + "The chest is locked.");
                    }
                } else if (e.getClickedBlock().getType() == Material.GLOWSTONE && p.isOp()) {
                    Location loc = e.getClickedBlock().getLocation();
                    if (e.getAction() == Action.RIGHT_CLICK_BLOCK && LootChests.getPlayerTier(p) > 0) {
                        e.setCancelled(true);
                        loot.put(loc, LootChests.getPlayerTier(p));
                        p.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "     *** LOOT CHEST CREATED ***");
                        loc.getWorld().getBlockAt(loc).setType(Material.CHEST);
                        loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.CHEST);
                        File file = new File(PracticeServer.plugin.getDataFolder(), "loot.yml");
                        if (file.exists()) {
                            file.delete();
                        }
                        YamlConfiguration config = new YamlConfiguration();
                        for (Location loc1 : loot.keySet()) {
                            String s = String.valueOf(loc1.getWorld().getName()) + "," + (int) loc1.getX() + "," + (int) loc1.getY() + "," + (int) loc1.getZ();
                            config.set(s, loot.get(loc1));
                            try {
                                config.save(file);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Location loc;
        Player p = e.getPlayer();
        if (p.isOp() && e.getBlock().getType().equals(Material.GLOWSTONE) && loot.containsKey((loc = e.getBlock().getLocation()))) {
            loot.remove(loc);
            p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "     *** LOOT CHEST REMOVED ***");
            loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.CHEST);
        }
    }

    public static int getPlayerTier(Player e) {
        ItemStack is = e.getItemInHand();
        if (is != null && is.getType() != Material.AIR) {
            if (is.getType().name().contains("WOOD_")) {
                return 1;
            }
            if (is.getType().name().contains("STONE_")) {
                return 2;
            }
            if (is.getType().name().contains("IRON_")) {
                return 3;
            }
            if (is.getType().name().contains("DIAMOND_")) {
                return 4;
            }
            if (is.getType().name().contains("GOLD_")) {
                return 5;
            }
        }
        return 0;
    }

    @EventHandler
    public void onCloseChest(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player p = (Player) e.getPlayer();
            if (e.getInventory().getName().contains("Loot Chest") && this.viewers.containsKey(p)) {
                Location loc = this.viewers.get(p);
                this.viewers.remove(p);
                p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1.0f, 1.0f);
                boolean isempty = true;
                ItemStack[] arritemStack = e.getInventory().getContents();
                int n = arritemStack.length;
                int n2 = 0;
                while (n2 < n) {
                    ItemStack itms = arritemStack[n2];
                    if (itms != null && itms.getType() != Material.AIR) {
                        isempty = false;
                    }
                    ++n2;
                }
                if (isempty) {
                    loc.getWorld().getBlockAt(loc).setType(Material.AIR);
                    loc.getWorld().playEffect(loc, Effect.STEP_SOUND, Material.WOOD);
                    p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 0.5f, 1.2f);
                    this.opened.remove(loc);
                    int tier = loot.get(loc);
                    respawn.put(loc, 60 * tier);
                    for (Player v : this.viewers.keySet()) {
                        if (!this.viewers.get(v).equals(loc)) continue;
                        this.viewers.remove(v);
                        v.closeInventory();
                        v.playSound(v.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, 0.5f, 1.2f);
                        v.playSound(v.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1.0f, 1.0f);
                    }
                }
            }
        }
    }

}

